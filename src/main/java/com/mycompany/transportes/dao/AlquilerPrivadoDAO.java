package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.AlquilerPrivado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlquilerPrivadoDAO {

    public AlquilerPrivado obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM alquiler_privado WHERE id_alquiler = ?";
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

    public List<AlquilerPrivado> obtenerPorCliente(String dpi) throws SQLException {
        String sql = "SELECT * FROM alquiler_privado WHERE dpi_cliente = ?";
        List<AlquilerPrivado> lista = new ArrayList<>();
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

    public List<AlquilerPrivado> obtenerPendientes() throws SQLException {
        String sql = "SELECT * FROM alquiler_privado WHERE estado_pago = 'SOLICITADO'";
        List<AlquilerPrivado> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<AlquilerPrivado> obtenerPorFechas(String inicio, String fin) throws SQLException {
        String sql = "SELECT * FROM alquiler_privado WHERE fecha_salida BETWEEN ? AND ?";
        List<AlquilerPrivado> lista = new ArrayList<>();
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

    public int crear(AlquilerPrivado a) throws SQLException {
        String sql = "INSERT INTO alquiler_privado (dpi_cliente, id_viaje, origen, destino, fecha_salida, fecha_retorno, numero_pasajeros, precio_estimado, estado_pago) VALUES (?, NULL, ?, ?, ?, ?, ?, ?, 'SOLICITADO')";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getDpiCliente());
            ps.setString(2, a.getOrigen());
            ps.setString(3, a.getDestino());
            ps.setString(4, a.getFechaSalida());
            ps.setString(5, a.getFechaRetorno());
            ps.setInt(6, a.getNumeroPasajeros());
            ps.setDouble(7, a.getPrecioEstimado());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void confirmarPrecio(int id, double precioConfirmado, double salarioChofer) throws SQLException {
        String sql = "UPDATE alquiler_privado SET precio_confirmado = ?, salario_chofer_calculado = ?, estado_pago = 'CONFIRMADO' WHERE id_alquiler = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, precioConfirmado);
            ps.setDouble(2, salarioChofer);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void asignarBus(int id, int idViaje) throws SQLException {
        String sql = "UPDATE alquiler_privado SET id_viaje = ? WHERE id_alquiler = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void pagar(int id) throws SQLException {
        String sql = "UPDATE alquiler_privado SET estado_pago = 'PAGADO', fecha_pago = NOW() WHERE id_alquiler = ?";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private AlquilerPrivado mapear(ResultSet rs) throws SQLException {
        AlquilerPrivado a = new AlquilerPrivado();
        a.setIdAlquiler(rs.getInt("id_alquiler"));
        a.setDpiCliente(rs.getString("dpi_cliente"));
        a.setIdViaje(rs.getInt("id_viaje"));
        a.setOrigen(rs.getString("origen"));
        a.setDestino(rs.getString("destino"));
        a.setFechaSalida(rs.getString("fecha_salida"));
        a.setFechaRetorno(rs.getString("fecha_retorno"));
        a.setNumeroPasajeros(rs.getInt("numero_pasajeros"));
        a.setPrecioEstimado(rs.getDouble("precio_estimado"));
        a.setPrecioConfirmado(rs.getDouble("precio_confirmado"));
        a.setSalarioChoferCalculado(rs.getDouble("salario_chofer_calculado"));
        a.setEstadoPago(rs.getString("estado_pago"));
        a.setFechaPago(rs.getString("fecha_pago"));
        return a;
    }
}
