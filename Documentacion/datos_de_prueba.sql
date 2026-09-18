
USE transporte_extraurbano;


-- 1. SUCURSAL (3)
INSERT INTO sucursal (id_sucursal, nombre, direccion, telefono, estado) VALUES
(1, 'Guatemala',      'Km 18, Carretera al Pacífico, Zona 4, Villa Nueva', '2233-4455', TRUE),
(2, 'Quetzaltenango', '7ª Avenida 12-40, Zona 1',                          '7765-4433', TRUE),
(3, 'Cobán',          '1ª Calle 5-67, Zona 3, Alta Verapaz',               '7952-1122', TRUE);


-- 2. USUARIO (2 ADMIN_SISTEMA + 3 ADMIN_SUCURSAL + 9 CHOFER + 3 CLIENTE)
INSERT INTO usuario
    (dpi, nombre_completo, nit, telefono, direccion, correo, password, rol, saldo_cartera, id_sucursal_origen, estado)
VALUES
-- ADMIN_SISTEMA (sin sucursal)
('2000000000001', 'Carlos Méndez Ramírez',    'NIT-AS1', '5555-0101', 'Ciudad de Guatemala', 'adminsistema1@123', '123', 'ADMIN_SISTEMA',   0.00, NULL, TRUE),
('2000000000002', 'María López Fuentes',      'NIT-AS2', '5555-0102', 'Ciudad de Guatemala', 'adminsistema2@123', '123', 'ADMIN_SISTEMA',   0.00, NULL, TRUE),

-- ADMIN_SUCURSAL (uno por sucursal)
('3000000000001', 'Ana García Morales',       'NIT-AD1', '5555-1101', 'Villa Nueva, Guatemala',      'adminsucursal1@123', '123', 'ADMIN_SUCURSAL', 0.00, 1, TRUE),
('3000000000002', 'José Pérez Castillo',      'NIT-AD2', '5555-1102', 'Quetzaltenango',              'adminsucursal2@123', '123', 'ADMIN_SUCURSAL', 0.00, 2, TRUE),
('3000000000003', 'Lucía Hernández Solís',    'NIT-AD3', '5555-1103', 'Cobán, Alta Verapaz',         'adminsucursal3@123', '123', 'ADMIN_SUCURSAL', 0.00, 3, TRUE),

-- CHOFER (3 por sucursal, id_sucursal_origen = sucursal)
('4000000000101', 'Pedro Jiménez Cruz',       'NIT-C01', '5555-1201', 'Guatemala',            'chofer1@123', '123', 'CHOFER', 0.00, 1, TRUE),
('4000000000102', 'Luis Morales Ixcot',       'NIT-C02', '5555-1202', 'Mixco, Guatemala',     'chofer2@123', '123', 'CHOFER', 0.00, 1, TRUE),
('4000000000103', 'Héctor Chávez López',      'NIT-C03', '5555-1203', 'San Juan Sacatepéquez', 'chofer3@123', '123', 'CHOFER', 0.00, 1, TRUE),
('4000000000104', 'Óscar Ramírez Florencio',  'NIT-C04', '5555-1204', 'Quetzaltenango',       'chofer4@123', '123', 'CHOFER', 0.00, 2, TRUE),
('4000000000105', 'Marco Tulio Bautista',     'NIT-C05', '5555-1205', 'San Marcos',           'chofer5@123', '123', 'CHOFER', 0.00, 2, TRUE),
('4000000000106', 'Rodrigo Sales Cano',       'NIT-C06', '5555-1206', 'Quetzaltenango',       'chofer6@123', '123', 'CHOFER', 0.00, 2, TRUE),
('4000000000107', 'Manuel Cucul Coy',         'NIT-C07', '5555-1207', 'Cobán, Alta Verapaz',  'chofer7@123', '123', 'CHOFER', 0.00, 3, TRUE),
('4000000000108', 'Antonio Pop Tun',          'NIT-C08', '5555-1208', 'Carchá, Alta Verapaz', 'chofer8@123', '123', 'CHOFER', 0.00, 3, TRUE),
('4000000000109', 'Jorge Sajbochol Maaz',     'NIT-C09', '5555-1209', 'Cobán, Alta Verapaz',  'chofer9@123', '123', 'CHOFER', 0.00, 3, TRUE),

-- CLIENTE (para comprar boletos, con saldo inicial)
('5000000000001', 'Sofía Ramírez Vega',       'NIT-CL1', '5555-1301', 'Guatemala',            'cliente1@123', '123', 'CLIENTE', 300.00, NULL, TRUE),
('5000000000002', 'Gabriela Castillo Reyes',  'NIT-CL2', '5555-1302', 'Quetzaltenango',       'cliente2@123', '123', 'CLIENTE', 300.00, NULL, TRUE),
('5000000000003', 'Fernando Martínez Ordóñez','NIT-CL3', '5555-1303', 'Cobán, Alta Verapaz',  'cliente3@123', '123', 'CLIENTE', 300.00, NULL, TRUE);


-- 3. CHOFER (9, mismo DPI que el usuario CHOFER)
INSERT INTO chofer
    (dpi, foto, num_licencia, tipo_licencia, fecha_vencimiento_licencia, salario_base_viaje, disponibilidad)
VALUES
('4000000000101', NULL, 'LIC-C01', 'A', '2029-03-15', 150.00, 'Disponible'),
('4000000000102', NULL, 'LIC-C02', 'B', '2027-01-20', 150.00, 'Disponible'),
('4000000000103', NULL, 'LIC-C03', 'A', '2029-08-10', 160.00, 'Disponible'),
('4000000000104', NULL, 'LIC-C04', 'B', '2027-11-05', 155.00, 'Disponible'),
('4000000000105', NULL, 'LIC-C05', 'A', '2028-05-30', 150.00, 'Disponible'),
('4000000000106', NULL, 'LIC-C06', 'A', '2029-01-25', 145.00, 'Disponible'),
('4000000000107', NULL, 'LIC-C07', 'B', '2027-07-18', 150.00, 'Disponible'),
('4000000000108', NULL, 'LIC-C08', 'A', '2028-12-12', 160.00, 'Disponible'),
('4000000000109', NULL, 'LIC-C09', 'B', '2029-04-02', 150.00, 'Disponible');


-- 4. BUS (9, tres por sucursal)

INSERT INTO bus
    (id_bus, placa, foto, marca, modelo, `año_fabricacion`, capacidad_pasajeros,
     kilometraje_actual, id_sucursal_origen, id_sucursal_actual, disponibilidad, estado)
VALUES
(1, 'M-1234', NULL, 'Volvo',          '9700',     2021, 45, 0.00, 1, 1, 'Disponible', TRUE),
(2, 'M-2345', NULL, 'Mercedes-Benz',  'Marcopolo',2020, 45, 0.00, 1, 1, 'Disponible', TRUE),
(3, 'M-3456', NULL, 'Marcopolo',      'Paradiso', 2022, 45, 0.00, 1, 1, 'Disponible', TRUE),
(4, 'M-4567', NULL, 'Volvo',          '9800',     2021, 45, 0.00, 2, 2, 'Disponible', TRUE),
(5, 'M-5678', NULL, 'Mercedes-Benz',  'Marcopolo',2020, 45, 0.00, 2, 2, 'Disponible', TRUE),
(6, 'M-6789', NULL, 'Marcopolo',      'Paradiso', 2023, 45, 0.00, 2, 2, 'Disponible', TRUE),
(7, 'M-7890', NULL, 'Volvo',          '9700',     2022, 45, 0.00, 3, 3, 'Disponible', TRUE),
(8, 'M-8901', NULL, 'Mercedes-Benz',  'Marcopolo',2021, 45, 0.00, 3, 3, 'Disponible', TRUE),
(9, 'M-9012', NULL, 'Marcopolo',      'Paradiso', 2022, 45, 0.00, 3, 3, 'Disponible', TRUE);


-- 5. RUTA (3, solo origen y destino distinto)
--    ejemplo de Guate->Xela = 200 km Q125 | Xela->Coban = 270 km Q160 | Coban->Guate = 250 km Q150
INSERT INTO ruta (id_ruta, id_sucursal_origen, id_sucursal_destino, distancia_km, precio_boleto, estado) VALUES
(1, 1, 2, 200.00, 125.00, TRUE),
(2, 2, 3, 270.00, 160.00, TRUE),
(3, 3, 1, 250.00, 150.00, TRUE);


-- 6. VIAJE
--    Viaje 1: Guate->Xela (200 km, ~3.5 h)  salida 20:00  llegada 23:30
--    Viaje 2: Xela->Coban (270 km, ~6 h)    salida 21:00  llegada 03:00 (del dia siguiente)
--    Viaje 3: Coban->Guate (250 km, ~3.5 h) salida 22:00  llegada 01:30 (del dia siguiente)

INSERT INTO viaje
    (id_viaje, id_bus, dpi_chofer, id_ruta, tipo_viaje,
     fecha_hora_salida_estimada, fecha_hora_llegada_estimada, estado_operativo)
VALUES
(1, 1, '4000000000101', 1, 'REGULAR', '2026-09-14 20:00:00', '2026-09-14 23:30:00', 'PROGRAMADO'),
(2, 4, '4000000000104', 2, 'REGULAR', '2026-09-14 21:00:00', '2026-09-15 03:00:00', 'PROGRAMADO'),
(3, 7, '4000000000107', 3, 'REGULAR', '2026-09-14 22:00:00', '2026-09-15 01:30:00', 'PROGRAMADO');


-- 7. BOLETO
--    Asientos ocupados del Viaje 1: 1, 2 (cliente1), 3 (cliente2), 7 (cliente3)

INSERT INTO boleto (id_boleto, id_viaje, dpi_cliente, num_asiento, precio_pagado, fecha_pago) VALUES
(1, 1, '5000000000001', 1, 125.00, NOW()),
(2, 1, '5000000000001', 2, 125.00, NOW()),
(3, 1, '5000000000002', 3, 125.00, NOW()),
(4, 1, '5000000000003', 7, 125.00, NOW());
