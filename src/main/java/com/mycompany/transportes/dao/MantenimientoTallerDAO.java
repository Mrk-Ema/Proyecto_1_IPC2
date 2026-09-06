package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Mantenimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoTallerDAO {

    public List<Mantenimiento> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM mantenimiento_taller";
        List<Mantenimiento> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Mantenimiento> obtenerPorBus(int idBus) throws SQLException {
        String sql = "SELECT * FROM mantenimiento_taller WHERE id_bus = ?";
        List<Mantenimiento> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Mantenimiento> obtenerPorFechas(String inicio, String fin) throws SQLException {
        String sql = "SELECT * FROM mantenimiento_taller WHERE fecha_mantenimiento BETWEEN ? AND ?";
        List<Mantenimiento> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inicio);
            ps.setString(2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public int crear(Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO mantenimiento_taller (id_bus, monto_mano_obra, monto_repuestos, fecha_mantenimiento, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getIdBus());
            ps.setDouble(2, m.getMontoManoObra());
            ps.setDouble(3, m.getMontoRepuestos());
            ps.setDate(4, m.getFechaMantenimiento());
            ps.setString(5, m.getDescripcion());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public double totalRepuestosPorBus(int idBus) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto_repuestos), 0) FROM mantenimiento_taller WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    public double totalManoDeObraPorBus(int idBus) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto_mano_obra), 0) FROM mantenimiento_taller WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    private Mantenimiento mapear(ResultSet rs) throws SQLException {
        Mantenimiento m = new Mantenimiento();
        m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
        m.setIdBus(rs.getInt("id_bus"));
        m.setMontoManoObra(rs.getDouble("monto_mano_obra"));
        m.setMontoRepuestos(rs.getDouble("monto_repuestos"));
        m.setFechaMantenimiento(rs.getDate("fecha_mantenimiento"));
        m.setDescripcion(rs.getString("descripcion"));
        return m;
    }
}
