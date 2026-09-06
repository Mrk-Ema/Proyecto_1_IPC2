package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario obtenerPorDpi(String dpi) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public Usuario obtenerPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Usuario> obtenerPorSucursal(int idSucursal) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_sucursal_origen = ?";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Usuario> obtenerPorRol(String rol) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE rol = ?";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public void crear(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (dpi, nombre_completo, nit, telefono, direccion, correo, password, rol, saldo_cartera, id_sucursal_origen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getNit());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getDireccion());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getPassword());
            ps.setString(8, u.getRol());
            ps.setDouble(9, u.getSaldoCartera());
            if (u.getIdSucursalOrigen() > 0) {
                ps.setInt(10, u.getIdSucursalOrigen());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
        }
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET nombre_completo = ?, nit = ?, telefono = ?, direccion = ?, correo = ? WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getNit());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getDireccion());
            ps.setString(5, u.getCorreo());
            ps.setString(6, u.getDpi());
            ps.executeUpdate();
        }
    }

    public void actualizarSaldo(String dpi, double saldo) throws SQLException {
        String sql = "UPDATE usuario SET saldo_cartera = ? WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, saldo);
            ps.setString(2, dpi);
            ps.executeUpdate();
        }
    }

    public void desactivar(String dpi) throws SQLException {
        String sql = "UPDATE usuario SET estado = FALSE WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            ps.executeUpdate();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setDpi(rs.getString("dpi"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setNit(rs.getString("nit"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setSaldoCartera(rs.getDouble("saldo_cartera"));
        u.setIdSucursalOrigen(rs.getInt("id_sucursal_origen"));
        u.setEstado(rs.getBoolean("estado"));
        return u;
    }
}
