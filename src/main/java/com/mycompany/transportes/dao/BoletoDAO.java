package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Boleto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoletoDAO {

    public Boleto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM boleto WHERE id_boleto = ?";
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

    public List<Boleto> obtenerPorViaje(int idViaje) throws SQLException {
        String sql = "SELECT * FROM boleto WHERE id_viaje = ?";
        List<Boleto> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Boleto> obtenerPorCliente(String dpi) throws SQLException {
        String sql = "SELECT * FROM boleto WHERE dpi_cliente = ?";
        List<Boleto> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Integer> asientosOcupados(int idViaje) throws SQLException {
        String sql = "SELECT num_asiento FROM boleto WHERE id_viaje = ?";
        List<Integer> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("num_asiento"));
                }
            }
        }
        return lista;
    }

    public int contarPorViaje(int idViaje) throws SQLException {
        String sql = "SELECT COUNT(*) FROM boleto WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int crear(Boleto b) throws SQLException {
        String sql = "INSERT INTO boleto (id_viaje, dpi_cliente, num_asiento, precio_pagado, fecha_pago) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getIdViaje());
            ps.setString(2, b.getDpiCliente());
            ps.setInt(3, b.getNumAsiento());
            ps.setDouble(4, b.getPrecioPagado());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    private Boleto mapear(ResultSet rs) throws SQLException {
        Boleto b = new Boleto();
        b.setIdBoleto(rs.getInt("id_boleto"));
        b.setIdViaje(rs.getInt("id_viaje"));
        b.setDpiCliente(rs.getString("dpi_cliente"));
        b.setNumAsiento(rs.getInt("num_asiento"));
        b.setPrecioPagado(rs.getDouble("precio_pagado"));
        b.setFechaPago(rs.getString("fecha_pago"));
        return b;
    }
}
