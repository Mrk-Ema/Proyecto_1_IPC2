package com.mycompany.transportes.servicio;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.dao.AlquilerPrivadoDAO;
import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.modelo.AlquilerPrivado;
import com.mycompany.transportes.modelo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlquilerService {

    // las tairufas 
    public static final double TARIFA_POR_HORA = 100.0;
    public static final double COSTO_POR_PASAJERO = 45.0;
    public static final int CAPACIDAD_MAX_BUS = 100;

    private final AlquilerPrivadoDAO alquilerDAO = new AlquilerPrivadoDAO();

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public enum EstadoPago {
        OK, CONTRASENA_INCORRECTA, SALDO_INSUFICIENTE, NO_CONFIRMADO, NO_ENCONTRADO, ERROR_SISTEMA
    }

    public static class ResultadoPagar {

        public EstadoPago estado;
        public AlquilerPrivado alquiler;
        public double faltante;
        public double nuevoSaldo;
    }

    public List<AlquilerPrivado> misAlquileres(String dpi) throws SQLException {
        return alquilerDAO.obtenerPorCliente(dpi);
    }

    public ResultadoPagar pagar(int idAlquiler, String dpi, String password) {
        ResultadoPagar r = new ResultadoPagar();
        Usuario usuario = null;
        try {
            usuario = usuarioDAO.obtenerPorDpi(dpi);
            r.alquiler = alquilerDAO.obtenerPorId(idAlquiler);
        } catch (SQLException e) {
            e.printStackTrace();
            r.estado = EstadoPago.ERROR_SISTEMA;
            return r;
        }

        if (usuario == null || r.alquiler == null || !r.alquiler.getDpiCliente().equals(dpi)) {
            r.estado = EstadoPago.NO_ENCONTRADO;
            return r;
        }
        if (!"CONFIRMADO".equals(r.alquiler.getEstadoPago())) {
            r.estado = EstadoPago.NO_CONFIRMADO;
            return r;
        }
        if (!usuario.getPassword().equals(password)) {
            r.estado = EstadoPago.CONTRASENA_INCORRECTA;
            return r;
        }

        double precio = r.alquiler.getPrecioConfirmado();
        if (usuario.getSaldoCartera() < precio) {
            r.faltante = precio - usuario.getSaldoCartera();
            r.estado = EstadoPago.SALDO_INSUFICIENTE;
            return r;
        }

        r.nuevoSaldo = usuario.getSaldoCartera() - precio;
        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);
            try {
                usuarioDAO.actualizarSaldo(conn, dpi, r.nuevoSaldo);
                alquilerDAO.pagar(conn, idAlquiler);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            r.estado = EstadoPago.OK;
        } catch (Exception e) {
            e.printStackTrace();
            r.estado = EstadoPago.ERROR_SISTEMA;
        }
        return r;
    }

    // Formula del precio estimado: horas × 150 + pasajeros × 50
    public double calcularPrecioEstimado(String fechaSalida, String fechaRetorno, int numeroPasajeros) {
        if (numeroPasajeros < 1 || numeroPasajeros > CAPACIDAD_MAX_BUS) {
            throw new IllegalArgumentException("El número de pasajeros debe estar entre 1 y " + CAPACIDAD_MAX_BUS + ".");
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime salida = LocalDateTime.parse(fechaSalida, formato);
        LocalDateTime retorno = LocalDateTime.parse(fechaRetorno, formato);

        if (salida.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de salida debe ser hoy o en el futuro.");
        }

        if (!retorno.isAfter(salida)) {
            throw new IllegalArgumentException("La fecha de retorno debe ser después de la fecha de salida.");
        }

        double horas = Duration.between(salida, retorno).toMinutes() / 60.0;
        double precio = (horas * TARIFA_POR_HORA) + (numeroPasajeros * COSTO_POR_PASAJERO);
        return Math.round(precio * 100.0) / 100.0;
    }

    // Guarda la solicitud en BD y devuelve el id generado
    public int solicitar(AlquilerPrivado a) throws SQLException {
        return alquilerDAO.crear(a);
    }

    public List<AlquilerPrivado> obtenerAlquileresPendientes() throws SQLException {
        return alquilerDAO.obtenerPendientes();
    }

    public void confirmarPrecio(int id, double precioConfirmado) throws SQLException {
        alquilerDAO.confirmarPrecio(id, precioConfirmado);
    }

    public void rechazarAlquiler(int id) throws SQLException {
        alquilerDAO.rechazar(id);
    }
}
