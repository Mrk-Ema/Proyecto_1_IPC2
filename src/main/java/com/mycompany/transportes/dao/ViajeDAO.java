package com.mycompany.transportes.dao;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.modelo.Viaje;
import com.mycompany.transportes.modelo.ViajeDisponible;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViajeDAO {

    public static final String VIAJES_DISPONIBLES_SELECT
            = "SELECT "
            + "    v.id_viaje, "
            + "    v.id_ruta, "
            + "    v.fecha_hora_salida_estimada, "
            + "    v.fecha_hora_llegada_estimada, "
            + "    so.nombre AS nombre_origen, "
            + "    sd.nombre AS nombre_destino, "
            + "    r.precio_boleto, "
            + "    b.placa AS placa_bus, "
            + "    b.capacidad_pasajeros "
            + "FROM viaje v "
            + "INNER JOIN ruta r ON v.id_ruta = r.id_ruta "
            + "INNER JOIN sucursal so ON r.id_sucursal_origen = so.id_sucursal "
            + "INNER JOIN sucursal sd ON r.id_sucursal_destino = sd.id_sucursal "
            + "INNER JOIN bus b ON v.id_bus = b.id_bus "
            + "WHERE v.tipo_viaje = 'REGULAR' "
            + "  AND v.estado_operativo = 'PROGRAMADO' "
            + "  AND r.estado = TRUE "
            + "  AND b.estado = TRUE "
            + "  AND so.estado = TRUE "
            + "  AND sd.estado = TRUE ";

    public List<ViajeDisponible> obtenerDisponiblesDetallado() throws SQLException {
        String sql = VIAJES_DISPONIBLES_SELECT + " ORDER BY v.fecha_hora_salida_estimada ASC";
        List<ViajeDisponible> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearDisponible(rs));
            }
        }
        return lista;
    }

    public ViajeDisponible obtenerDisponibleDetalladoPorId(int idViaje) throws SQLException {
        String sql = VIAJES_DISPONIBLES_SELECT + " AND v.id_viaje = ?";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDisponible(rs);
                }
            }
        }
        return null;   // null = ya no existe o no está PROGRAMADO o  talves ruta o bus inactivo
    }

    private ViajeDisponible mapearDisponible(ResultSet rs) throws SQLException {
        ViajeDisponible vd = new ViajeDisponible();
        vd.setIdViaje(rs.getInt("id_viaje"));
        vd.setIdRuta(rs.getInt("id_ruta"));
        vd.setFechaHoraSalida(rs.getString("fecha_hora_salida_estimada"));
        vd.setFechaHoraLlegada(rs.getString("fecha_hora_llegada_estimada"));
        vd.setNombreOrigen(rs.getString("nombre_origen"));
        vd.setNombreDestino(rs.getString("nombre_destino"));
        vd.setPrecioBoleto(rs.getDouble("precio_boleto"));
        vd.setPlacaBus(rs.getString("placa_bus"));
        vd.setCapacidadPasajeros(rs.getInt("capacidad_pasajeros"));
        vd.setAsientosOcupados(new BoletoDAO().asientosOcupados(vd.getIdViaje()));
        return vd;
    }

    public List<Viaje> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM viaje";
        List<Viaje> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Viaje obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM viaje WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Viaje> obtenerPorSucursalConDetalles(int idSucursal) throws SQLException {
        String sql = "SELECT v.*, b.placa AS placa_bus, u.nombre_completo AS nombre_chofer, "
                + "so.nombre AS nombre_origen, sd.nombre AS nombre_destino "
                + "FROM viaje v "
                + "INNER JOIN bus b ON v.id_bus = b.id_bus "
                + "LEFT JOIN chofer c ON v.dpi_chofer = c.dpi "
                + "LEFT JOIN usuario u ON c.dpi = u.dpi "
                + "LEFT JOIN ruta r ON v.id_ruta = r.id_ruta "
                + "LEFT JOIN sucursal so ON r.id_sucursal_origen = so.id_sucursal "
                + "LEFT JOIN sucursal sd ON r.id_sucursal_destino = sd.id_sucursal "
                + "WHERE b.id_sucursal_actual = ? "
                + "ORDER BY v.fecha_hora_salida_estimada DESC";
        List<Viaje> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearConDetalles(rs));
                }
            }
        }
        return lista;
    }

    public int contarViajesActivos(int idBus) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaje WHERE id_bus = ? AND estado_operativo IN ('PROGRAMADO', 'EN_TRANSITO')";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int contarViajesActivosDeChofer(String dpi) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaje WHERE dpi_chofer = ? AND estado_operativo IN ('PROGRAMADO', 'EN_TRANSITO')";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dpi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int contarViajesDeRuta(int idRuta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaje WHERE id_ruta = ?";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Viaje> obtenerPorFechas(String inicio, String fin) throws SQLException {
        String sql = "SELECT * FROM viaje WHERE fecha_hora_salida_estimada BETWEEN ? AND ?";
        List<Viaje> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, v.getIdBus());

            ps.setString(2, v.getDpiChofer());

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
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaReal);
            ps.setDouble(2, kmInicial);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void registrarLlegada(int id, String fechaReal, double kmFinal, double gastoCombustible, double depreciacion) throws SQLException {
        String sql = "UPDATE viaje SET fecha_hora_llegada_real = ?, kilometraje_final = ?, gasto_combustible = ?, monto_depreciacion = ?, estado_operativo = 'FINALIZADO' WHERE id_viaje = ?";
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = Conexion.obtener(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Viaje mapearConDetalles(ResultSet rs) throws SQLException {
        Viaje v = mapear(rs);
        v.setPlacaBus(rs.getString("placa_bus"));
        v.setNombreChofer(rs.getString("nombre_chofer"));
        String origen = rs.getString("nombre_origen");
        String destino = rs.getString("nombre_destino");
        v.setNombreRuta(origen != null && destino != null ? origen + " → " + destino : null);
        return v;
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
