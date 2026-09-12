package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Chofer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoferDAO {

    public List<Chofer> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM chofer";
        List<Chofer> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }  

    public Chofer obtenerPorDpi(String dpi) throws SQLException {
        String sql = "SELECT * FROM chofer WHERE dpi = ?";
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

    public List<Chofer> obtenerPorSucursal(int idSucursal) throws SQLException {
        String sql = "SELECT c.* FROM chofer c INNER JOIN usuario u ON c.dpi = u.dpi WHERE u.id_sucursal_origen = ?";
        List<Chofer> lista = new ArrayList<>();
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

    public List<Chofer> obtenerDisponibles(int idSucursal, String fecha) throws SQLException {
        String sql = "SELECT c.* FROM chofer c "
                + "INNER JOIN usuario u ON c.dpi = u.dpi "
                + "WHERE u.id_sucursal_origen = ? AND u.estado = TRUE AND c.disponibilidad = 'Disponible' "
                + "AND c.dpi NOT IN (SELECT dpi_chofer FROM viaje WHERE DATE(fecha_hora_salida_estimada) = ? "
                + "AND estado_operativo IN ('PROGRAMADO', 'EN_TRANSITO'))";
        List<Chofer> lista = new ArrayList<>();
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

    public void crear(Chofer c) throws SQLException {
        try (Connection conn = Conexion.obtener()) {
            crear(conn, c);
        }
    }

    public void crear(Connection conn, Chofer c) throws SQLException {
        String sql = "INSERT INTO chofer (dpi, foto, num_licencia, tipo_licencia, fecha_vencimiento_licencia, salario_base_viaje) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDpi());
            ps.setString(2, c.getFoto());
            ps.setString(3, c.getNumLicencia());
            ps.setString(4, c.getTipoLicencia());
            ps.setDate(5, c.getFechaVencimientoLicencia());
            ps.setDouble(6, c.getSalarioBaseViaje());
            ps.executeUpdate();
        }
    }

    public void actualizar(Chofer c) throws SQLException {
        String sql = "UPDATE chofer SET foto = ?, num_licencia = ?, tipo_licencia = ?, fecha_vencimiento_licencia = ?, salario_base_viaje = ? WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFoto());
            ps.setString(2, c.getNumLicencia());
            ps.setString(3, c.getTipoLicencia());
            ps.setDate(4, c.getFechaVencimientoLicencia());
            ps.setDouble(5, c.getSalarioBaseViaje());
            ps.setString(6, c.getDpi());
            ps.executeUpdate();
        }
    }

    public void actualizarDisponibilidad(String dpi, String disponibilidad) throws SQLException {
        String sql = "UPDATE chofer SET disponibilidad = ? WHERE dpi = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, disponibilidad);
            ps.setString(2, dpi);
            ps.executeUpdate();
        }
    }

    private Chofer mapear(ResultSet rs) throws SQLException {
        Chofer c = new Chofer();
        c.setDpi(rs.getString("dpi"));
        c.setFoto(rs.getString("foto"));
        c.setNumLicencia(rs.getString("num_licencia"));
        c.setTipoLicencia(rs.getString("tipo_licencia"));
        c.setFechaVencimientoLicencia(rs.getDate("fecha_vencimiento_licencia"));
        c.setSalarioBaseViaje(rs.getDouble("salario_base_viaje"));
        c.setDisponibilidad(rs.getString("disponibilidad"));
        return c;
    }
}
