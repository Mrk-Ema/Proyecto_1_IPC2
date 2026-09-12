package com.mycompany.transportes.servicio;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.dao.ChoferDAO;
import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.modelo.Chofer;
import com.mycompany.transportes.modelo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ChoferDAO choferDAO = new ChoferDAO();

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }

    public Usuario autenticar(String correo, String password) throws SQLException {
        Usuario usuario = usuarioDAO.obtenerPorCorreo(correo);
        if (usuario != null && usuario.getPassword().equals(password) && usuario.isEstado()) {
            return usuario;
        }
        return null;
    }

    public void registrarCliente(Usuario usuario) throws SQLException, ValidacionException {
        validarCamposComunes(usuario);
        validarDuplicados(usuario.getDpi(), usuario.getCorreo());
        usuarioDAO.crear(usuario);
    }

    public void registrarAdminSucursal(Usuario usuario) throws SQLException, ValidacionException {
        validarCamposComunes(usuario);
        if (usuario.getIdSucursalOrigen() <= 0) {
            throw new ValidacionException("Debe seleccionar una sucursal válida.");
        }
        validarDuplicados(usuario.getDpi(), usuario.getCorreo());
        usuarioDAO.crear(usuario);
    }

    public void registrarChofer(Usuario usuario, Chofer chofer) throws SQLException, ValidacionException {
        validarCamposComunes(usuario);
        validarCamposChofer(chofer);
        validarDuplicados(usuario.getDpi(), usuario.getCorreo());

        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);
            try {
                usuarioDAO.crear(conn, usuario);
                choferDAO.crear(conn, chofer);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void validarDuplicados(String dpi, String correo) throws SQLException, ValidacionException {
        if (usuarioDAO.obtenerPorDpi(dpi) != null) {
            throw new ValidacionException("Ya existe una cuenta registrada con este número de DPI.");
        }
        if (usuarioDAO.obtenerPorCorreo(correo) != null) {
            throw new ValidacionException("Ya existe una cuenta registrada con este correo electrónico.");
        }
    }

    private void validarCamposComunes(Usuario u) throws ValidacionException {
        if (esVacio(u.getDpi()) || esVacio(u.getNombreCompleto()) || esVacio(u.getNit())
                || esVacio(u.getTelefono()) || esVacio(u.getDireccion())
                || esVacio(u.getCorreo()) || esVacio(u.getPassword())) {
            throw new ValidacionException("Por favor, llene todos los campos obligatorios.");
        }

        if (!u.getDpi().matches("\\d{13}")) {
            throw new ValidacionException("El DPI debe tener exactamente 13 dígitos.");
        }
        if (!u.getNit().matches("\\d{8}")) {
            throw new ValidacionException("El NIT debe tener exactamente 8 dígitos.");
        }
        if (!u.getTelefono().matches("\\d{8}")) {
            throw new ValidacionException("El teléfono debe tener exactamente 8 dígitos, sin espacios.");
        }
        if (!u.getCorreo().contains("@")) {
            throw new ValidacionException("El correo electrónico es inválido.");
        }
        if (u.getPassword().length() < 6) {
            throw new ValidacionException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    private void validarCamposChofer(Chofer c) throws ValidacionException {
        if (esVacio(c.getNumLicencia()) || esVacio(c.getTipoLicencia())
                || c.getFechaVencimientoLicencia() == null) {
            throw new ValidacionException("Por favor, llene todos los campos obligatorios del chofer.");
        }
        if (!c.getNumLicencia().matches("\\d{14}")) {
            throw new ValidacionException("El número de licencia debe tener 14 dígitos.");
        }
        if (c.getSalarioBaseViaje() <= 0) {
            throw new ValidacionException("El salario base debe ser mayor que 0.");
        }
        if (!c.getFechaVencimientoLicencia().after(new java.util.Date())) {
            throw new ValidacionException("La fecha de vencimiento de la licencia debe ser una fecha futura.");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}