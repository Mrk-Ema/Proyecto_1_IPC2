package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Viaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViajeDAO {

    public List<Viaje> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM viaje";
        List<Viaje> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Viaje obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM viaje WHERE id_viaje = ?";
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

    public List<Viaje> obtenerDisponibles(String fecha) throws SQLException {
        String sql = "SELECT * FROM viaje WHERE tipo_viaje = 'REGULAR' AND estado_operativo = 'PROGRAMADO' AND DATE(fecha_hora_salida_estimada) = ?";
        List<Viaje> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Viaje> obtenerPorSucursal(int idSucursal) throws SQLException {
        String sql = "SELECT v.* FROM viaje v INNER JOIN bus b ON v.id_bus = b.id_bus WHERE b.id_sucursal = ?";
        List<Viaje> lista = new ArrayList<>();
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

    public List<Viaje> obtenerPorFechas(String inicio, String fin) throws SQLException {
        String sql = "SELECT * FROM viaje WHERE fecha_hora_salida_estimada BETWEEN ? AND ?";
        List<Viaje> lista = new ArrayList<>();
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

    public int crear(Viaje v) throws SQLException {
        String sql = "INSERT INTO viaje (id_bus, dpi_chofer, id_ruta, tipo_viaje, fecha_hora_salida_estimada, fecha_hora_llegada_estimada, estado_operativo) VALUES (?, ?, ?, ?, ?, ?, 'PROGRAMADO')";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (v.getIdBus() > 0) {
                ps.setInt(1, v.getIdBus());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            if (v.getDpiChofer() != null && !v.getDpiChofer().isEmpty()) {
                ps.setString(2, v.getDpiChofer());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }
            if (v.getIdRuta() > 0) {
                ps.setInt(3, v.getIdRuta());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setString(4, v.getTipoViaje());
            ps.setString(5, v.getFechaHoraSalidaEstimada());
            ps.setString(6, v.getFechaHoraLlegadaEstimada());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void actualizar(Viaje v) throws SQLException {
        String sql = "UPDATE viaje SET id_bus = ?, dpi_chofer = ?, id_ruta = ?, fecha_hora_salida_estimada = ?, fecha_hora_llegada_estimada = ? WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (v.getIdBus() > 0) {
                ps.setInt(1, v.getIdBus());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            if (v.getDpiChofer() != null && !v.getDpiChofer().isEmpty()) {
                ps.setString(2, v.getDpiChofer());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }
            if (v.getIdRuta() > 0) {
                ps.setInt(3, v.getIdRuta());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setString(4, v.getFechaHoraSalidaEstimada());
            ps.setString(5, v.getFechaHoraLlegadaEstimada());
            ps.setInt(6, v.getIdViaje());
            ps.executeUpdate();
        }
    }

    public void registrarSalida(int id, String fechaReal, double kmInicial) throws SQLException {
        // Validar que el viaje tenga bus y chofer asignados antes de registrar salida
        Viaje v = obtenerPorId(id);
        if (v == null) {
            throw new SQLException("El viaje no existe.");
        }
        if (v.getIdBus() <= 0 || v.getDpiChofer() == null || v.getDpiChofer().isEmpty()) {
            throw new SQLException("No se puede registrar la salida: el viaje no tiene bus o chofer asignado.");
        }

        String sql = "UPDATE viaje SET fecha_hora_salida_real = ?, kilometraje_inicial = ?, estado_operativo = 'EN_TRANSITO' WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaReal);
            ps.setDouble(2, kmInicial);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void registrarLlegada(int id, String fechaReal, double kmFinal, double gastoCombustible, double depreciacion) throws SQLException {
        String sql = "UPDATE viaje SET fecha_hora_llegada_real = ?, kilometraje_final = ?, gasto_combustible = ?, monto_depreciacion = ?, estado_operativo = 'FINALIZADO' WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaReal);
            ps.setDouble(2, kmFinal);
            ps.setDouble(3, gastoCombustible);
            ps.setDouble(4, depreciacion);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM viaje WHERE id_viaje = ? AND estado_operativo = 'PROGRAMADO' AND id_viaje NOT IN (SELECT DISTINCT id_viaje FROM boleto WHERE id_viaje IS NOT NULL)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Viaje mapear(ResultSet rs) throws SQLException {
        Viaje v = new Viaje();
        v.setIdViaje(rs.getInt("id_viaje"));
        v.setIdBus(rs.getInt("id_bus"));
        v.setDpiChofer(rs.getString("dpi_chofer"));
        v.setIdRuta(rs.getInt("id_ruta"));
        v.setTipoViaje(rs.getString("tipo_viaje"));
        v.setFechaHoraSalidaEstimada(rs.getString("fecha_hora_salida_estimada"));
        v.setFechaHoraLlegadaEstimada(rs.getString("fecha_hora_llegada_estimada"));
        v.setFechaHoraSalidaReal(rs.getString("fecha_hora_salida_real"));
        v.setKilometrajeInicial(rs.getDouble("kilometraje_inicial"));
        v.setFechaHoraLlegadaReal(rs.getString("fecha_hora_llegada_real"));
        v.setKilometrajeFinal(rs.getDouble("kilometraje_final"));
        v.setGastoCombustible(rs.getDouble("gasto_combustible"));
        v.setMontoDepreciacion(rs.getDouble("monto_depreciacion"));
        v.setEstadoOperativo(rs.getString("estado_operativo"));
        return v;
    }
}
