CREATE DATABASE IF NOT EXISTS transporte_extraurbano;
USE transporte_extraurbano;

# 1. CONFIGURACION_SISTEMA
CREATE TABLE configuracion_sistema (
    id_config INT AUTO_INCREMENT PRIMARY KEY,
    monto_depreciacion_km DECIMAL(10,2) NOT NULL DEFAULT 1.50,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO configuracion_sistema (monto_depreciacion_km) VALUES (1.50);

# 2. SUCURSAL
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(15),
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

# 3. USUARIO
CREATE TABLE usuario (
    dpi VARCHAR(13) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    nit VARCHAR(15),
    telefono VARCHAR(15),
    direccion VARCHAR(150),
    correo VARCHAR(80) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN_SISTEMA', 'ADMIN_SUCURSAL', 'CLIENTE', 'CHOFER') NOT NULL,
    saldo_cartera DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_sucursal_origen INT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT FK_USUARIO_SUCURSAL FOREIGN KEY (id_sucursal_origen) 
        REFERENCES sucursal(id_sucursal) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
        CONSTRAINT CHK_SALDO_POSITIVO CHECK (saldo_cartera >= 0)
);

# 4. BUS
CREATE TABLE bus (
    id_bus INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) UNIQUE NOT NULL,
    foto VARCHAR(255) NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    año_fabricacion INT NOT NULL,
    capacidad_pasajeros INT NOT NULL,
    kilometraje_actual DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    id_sucursal_origen INT NOT NULL,
    id_sucursal_actual INT NOT NULL,
    disponibilidad ENUM('Disponible','Ocupado','Taller') NOT NULL DEFAULT 'Disponible',
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT FK_BUS_SUCURSAL_ORIGEN FOREIGN KEY (id_sucursal_origen) 
        REFERENCES sucursal(id_sucursal),
    CONSTRAINT FK_BUS_SUCURSAL_ACTUAL FOREIGN KEY (id_sucursal_actual) 
        REFERENCES sucursal(id_sucursal) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);

# 5. CHOFER
CREATE TABLE chofer (
    dpi VARCHAR(13) PRIMARY KEY,
    foto VARCHAR(255) NULL,
    num_licencia VARCHAR(20) NOT NULL,
    tipo_licencia ENUM('A', 'B') NOT NULL,
    fecha_vencimiento_licencia DATE NOT NULL,
    salario_base_viaje DECIMAL(10,2) NOT NULL,
    disponibilidad ENUM('Disponible','Ocupado') NOT NULL DEFAULT 'Disponible',
    
    CONSTRAINT FK_CHOFER_USUARIO FOREIGN KEY (dpi) 
        REFERENCES usuario(dpi) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);

# 6. RUTA
CREATE TABLE ruta (
    id_ruta INT AUTO_INCREMENT PRIMARY KEY,
    id_sucursal_origen INT NOT NULL,
    id_sucursal_destino INT NOT NULL,
    distancia_km DECIMAL(10,2) NOT NULL,
    precio_boleto DECIMAL(10,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT FK_RUTA_ORIGEN FOREIGN KEY (id_sucursal_origen) 
        REFERENCES sucursal(id_sucursal) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_RUTA_DESTINO FOREIGN KEY (id_sucursal_destino) 
        REFERENCES sucursal(id_sucursal) 
        ON DELETE RESTRICT ON UPDATE CASCADE
       
);

# 7. VIAJE
CREATE TABLE viaje (
    id_viaje INT AUTO_INCREMENT PRIMARY KEY,
    id_bus INT NOT NULL,
    dpi_chofer VARCHAR(13) NOT NULL,
    id_ruta INT NULL,
    tipo_viaje ENUM('REGULAR', 'ALQUILER_PRIVADO') NOT NULL,
    fecha_hora_salida_estimada DATETIME NOT NULL,
    fecha_hora_llegada_estimada DATETIME NOT NULL,
    fecha_hora_salida_real DATETIME NULL,
    kilometraje_inicial DECIMAL(12,2) NULL,
    fecha_hora_llegada_real DATETIME NULL,
    kilometraje_final DECIMAL(12,2) NULL,
    gasto_combustible DECIMAL(10,2) NULL,
    monto_depreciacion DECIMAL(10,2) NULL,
    estado_operativo ENUM('PROGRAMADO', 'EN_TRANSITO', 'FINALIZADO', 'CANCELADO') NOT NULL DEFAULT 'PROGRAMADO',
    
    CONSTRAINT FK_VIAJE_BUS FOREIGN KEY (id_bus) 
        REFERENCES bus(id_bus) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_VIAJE_CHOFER FOREIGN KEY (dpi_chofer) 
        REFERENCES chofer(dpi) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_VIAJE_RUTA FOREIGN KEY (id_ruta) 
        REFERENCES ruta(id_ruta) 
        ON DELETE RESTRICT ON UPDATE CASCADE
       
);

# 8. BOLETO
CREATE TABLE boleto (
    id_boleto INT AUTO_INCREMENT PRIMARY KEY,
    id_viaje INT NOT NULL,
    dpi_cliente VARCHAR(13) NOT NULL,
    num_asiento INT NOT NULL,
    precio_pagado DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT FK_BOLETO_VIAJE FOREIGN KEY (id_viaje) 
        REFERENCES viaje(id_viaje) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_BOLETO_CLIENTE FOREIGN KEY (dpi_cliente) 
        REFERENCES usuario(dpi) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_BOLETO_ASIENTO UNIQUE (id_viaje, num_asiento),
    CONSTRAINT CHK_ASIENTO_POSITIVO CHECK (num_asiento >= 1)
);

# 9. ALQUILER_PRIVADO
CREATE TABLE alquiler_privado (
    id_alquiler INT AUTO_INCREMENT PRIMARY KEY,
    dpi_cliente VARCHAR(13) NOT NULL,
    id_viaje INT NULL,
    origen VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_salida DATETIME NOT NULL,
    fecha_retorno DATETIME NULL,
    numero_pasajeros INT NOT NULL,
    precio_estimado DECIMAL(10,2) NOT NULL,
    precio_confirmado DECIMAL(10,2) NULL,
    salario_chofer_calculado DECIMAL(10,2) NULL,
    estado_pago ENUM('SOLICITADO', 'CONFIRMADO', 'PAGADO', 'RECHAZADO') NOT NULL DEFAULT 'SOLICITADO',
    fecha_pago DATETIME NULL,
    
    CONSTRAINT FK_ALQUILER_CLIENTE FOREIGN KEY (dpi_cliente) 
        REFERENCES usuario(dpi) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_ALQUILER_VIAJE FOREIGN KEY (id_viaje) 
        REFERENCES viaje(id_viaje) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);

# 10. MANTENIMIENTO_TALLER
CREATE TABLE mantenimiento_taller (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_bus INT NOT NULL,
    monto_mano_obra DECIMAL(10,2) NOT NULL,
    monto_repuestos DECIMAL(10,2) NOT NULL,
    fecha_mantenimiento DATE NOT NULL,
    descripcion TEXT,
    
    CONSTRAINT FK_MANTENIMIENTO_BUS FOREIGN KEY (id_bus) 
        REFERENCES bus(id_bus) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);
