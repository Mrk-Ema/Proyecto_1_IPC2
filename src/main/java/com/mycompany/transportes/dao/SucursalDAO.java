package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SucursalDAO {

    public List<Sucursal> obtenerTodas() throws SQLException {
        String sql = "SELECT * FROM sucursal";
        List<Sucursal> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Sucursal obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM sucursal WHERE id_sucursal = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public int crear(Sucursal s) throws SQLException {
        String sql = "INSERT INTO sucursal (nombre, direccion, telefono) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            ps.setString(3, s.getTelefono());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void actualizar(Sucursal s) throws SQLException {
        String sql = "UPDATE sucursal SET nombre = ?, direccion = ?, telefono = ? WHERE id_sucursal = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            ps.setString(3, s.getTelefono());
            ps.setInt(4, s.getIdSucursal());
            ps.executeUpdate();
        }
    }

    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE sucursal SET estado = FALSE WHERE id_sucursal = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Sucursal mapear(ResultSet rs) throws SQLException {
        Sucursal s = new Sucursal();
        s.setIdSucursal(rs.getInt("id_sucursal"));
        s.setNombre(rs.getString("nombre"));
        s.setDireccion(rs.getString("direccion"));
        s.setTelefono(rs.getString("telefono"));
        s.setEstado(rs.getBoolean("estado"));
        return s;
    }
}
