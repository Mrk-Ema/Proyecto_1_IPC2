package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionSistemaDAO {

    public double obtener() throws SQLException {
        String sql = "SELECT monto_depreciacion_km FROM configuracion_sistema WHERE id_config = 1";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("monto_depreciacion_km");
            }
        }
        return 0.0;
    }

    public void actualizar(double monto) throws SQLException {
        String sql = "UPDATE configuracion_sistema SET monto_depreciacion_km = ?, fecha_actualizacion = NOW() WHERE id_config = 1";
        try (Connection conn = Conexion.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, monto);
            ps.executeUpdate();
        }
    }
}
