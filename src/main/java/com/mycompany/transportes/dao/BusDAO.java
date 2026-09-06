package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Bus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    public List<Bus> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM bus";
        List<Bus> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Bus obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM bus WHERE id_bus = ?";
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

    public List<Bus> obtenerPorSucursal(int idSucursal) throws SQLException {
        String sql = "SELECT * FROM bus WHERE id_sucursal_actual = ?";
        List<Bus> lista = new ArrayList<>();
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

    public List<Bus> obtenerDisponibles(int idSucursal, String fecha) throws SQLException {
        String sql = "SELECT * FROM bus WHERE id_sucursal_actual = ? AND estado = TRUE AND disponibilidad = 'Disponible' "
                + "AND id_bus NOT IN (SELECT id_bus FROM viaje WHERE DATE(fecha_hora_salida_estimada) = ? "
                + "AND estado_operativo IN ('PROGRAMADO', 'EN_TRANSITO'))";
        List<Bus> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ps.setString(2, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public int crear(Bus b) throws SQLException {
        String sql = "INSERT INTO bus (placa, foto, marca, modelo, año_fabricacion, capacidad_pasajeros, kilometraje_actual, id_sucursal_origen, id_sucursal_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getPlaca());
            ps.setString(2, b.getFoto());
            ps.setString(3, b.getMarca());
            ps.setString(4, b.getModelo());
            ps.setInt(5, b.getAnoFabricacion());
            ps.setInt(6, b.getCapacidadPasajeros());
            ps.setDouble(7, b.getKilometrajeActual());
            ps.setInt(8, b.getIdSucursalOrigen());
            ps.setInt(9, b.getIdSucursalOrigen());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void actualizar(Bus b) throws SQLException {
        String sql = "UPDATE bus SET placa = ?, foto = ?, marca = ?, modelo = ?, año_fabricacion = ?, capacidad_pasajeros = ? WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getPlaca());
            ps.setString(2, b.getFoto());
            ps.setString(3, b.getMarca());
            ps.setString(4, b.getModelo());
            ps.setInt(5, b.getAnoFabricacion());
            ps.setInt(6, b.getCapacidadPasajeros());
            ps.setInt(7, b.getIdBus());
            ps.executeUpdate();
        }
    }

    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE bus SET estado = FALSE WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void actualizarKilometraje(int id, double km) throws SQLException {
        String sql = "UPDATE bus SET kilometraje_actual = ? WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, km);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void actualizarDisponibilidad(int id, String disponibilidad) throws SQLException {
        String sql = "UPDATE bus SET disponibilidad = ? WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, disponibilidad);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void actualizarUbicacion(int idBus, int idNuevaSucursal) throws SQLException {
        String sql = "UPDATE bus SET id_sucursal_actual = ? WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNuevaSucursal);
            ps.setInt(2, idBus);
            ps.executeUpdate();
        }
    }

    public void activar(int id) throws SQLException {
        String sql = "UPDATE bus SET estado = TRUE WHERE id_bus = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Bus mapear(ResultSet rs) throws SQLException {
        Bus b = new Bus();
        b.setIdBus(rs.getInt("id_bus"));
        b.setPlaca(rs.getString("placa"));
        b.setFoto(rs.getString("foto"));
        b.setMarca(rs.getString("marca"));
        b.setModelo(rs.getString("modelo"));
        b.setAnoFabricacion(rs.getInt("año_fabricacion"));
        b.setCapacidadPasajeros(rs.getInt("capacidad_pasajeros"));
        b.setKilometrajeActual(rs.getDouble("kilometraje_actual"));
        b.setIdSucursalOrigen(rs.getInt("id_sucursal_origen"));
        b.setIdSucursalActual(rs.getInt("id_sucursal_actual"));
        b.setDisponibilidad(rs.getString("disponibilidad"));
        b.setEstado(rs.getBoolean("estado"));
        return b;
    }
}
