package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Ruta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

    public List<Ruta> obtenerTodas() throws SQLException {
        String sql = "SELECT * FROM ruta";
        List<Ruta> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Ruta obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM ruta WHERE id_ruta = ?";
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

    public List<Ruta> obtenerPorSucursalOrigen(int idSucursal) throws SQLException {
        String sql = "SELECT * FROM ruta WHERE id_sucursal_origen = ?";
        List<Ruta> lista = new ArrayList<>();
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

    public List<Ruta> obtenerDisponibles() throws SQLException {
        String sql = "SELECT * FROM ruta WHERE estado = TRUE";
        List<Ruta> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public int crear(Ruta r) throws SQLException {
        String sql = "INSERT INTO ruta (id_sucursal_origen, id_sucursal_destino, distancia_km, precio_boleto, estado) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdSucursalOrigen());
            ps.setInt(2, r.getIdSucursalDestino());
            ps.setDouble(3, r.getDistanciaKm());
            ps.setDouble(4, r.getPrecioBoleto());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void actualizar(Ruta r) throws SQLException {
        String sql = "UPDATE ruta SET id_sucursal_origen = ?, id_sucursal_destino = ?, distancia_km = ?, precio_boleto = ? WHERE id_ruta = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdSucursalOrigen());
            ps.setInt(2, r.getIdSucursalDestino());
            ps.setDouble(3, r.getDistanciaKm());
            ps.setDouble(4, r.getPrecioBoleto());
            ps.setInt(5, r.getIdRuta());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM ruta WHERE id_ruta = ? AND id_ruta NOT IN (SELECT DISTINCT id_ruta FROM viaje WHERE id_ruta IS NOT NULL)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Ruta mapear(ResultSet rs) throws SQLException {
        Ruta r = new Ruta();
        r.setIdRuta(rs.getInt("id_ruta"));
        r.setIdSucursalOrigen(rs.getInt("id_sucursal_origen"));
        r.setIdSucursalDestino(rs.getInt("id_sucursal_destino"));
        r.setDistanciaKm(rs.getDouble("distancia_km"));
        r.setPrecioBoleto(rs.getDouble("precio_boleto"));
        r.setEstado(rs.getBoolean("estado"));
        return r;
    }
}
