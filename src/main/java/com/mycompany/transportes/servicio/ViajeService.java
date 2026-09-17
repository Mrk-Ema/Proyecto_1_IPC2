package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.BoletoDAO;
import com.mycompany.transportes.dao.BusDAO;
import com.mycompany.transportes.dao.ChoferDAO;
import com.mycompany.transportes.dao.ConfiguracionSistemaDAO;
import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.Bus;
import com.mycompany.transportes.modelo.Chofer;
import com.mycompany.transportes.modelo.Viaje;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class ViajeService {

    private static final DateTimeFormatter FORMATO_DB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ViajeDAO viajeDAO = new ViajeDAO();
    private final BusDAO busDAO = new BusDAO();
    private final ChoferDAO choferDAO = new ChoferDAO();
    private final BoletoDAO boletoDAO = new BoletoDAO();
    private final ConfiguracionSistemaDAO configDAO = new ConfiguracionSistemaDAO();

    public List<Viaje> listarPorSucursal(int idSucursal) throws SQLException {
        return viajeDAO.obtenerPorSucursalConDetalles(idSucursal);
    }

    public Viaje obtenerPorId(int id) throws SQLException {
        return viajeDAO.obtenerPorId(id);
    }

    public void crear(Viaje viaje, int idSucursal) throws SQLException, ValidacionException {
        validarDatos(viaje);
        validarDisponibilidad(viaje, idSucursal);
        viajeDAO.crear(viaje);
    }

    public void actualizar(Viaje viaje, int idSucursal) throws SQLException, ValidacionException {
        Viaje existente = viajeDAO.obtenerPorId(viaje.getIdViaje());
        if (existente == null) {
            throw new ValidacionException("El viaje no existe.");
        }
        if (!"PROGRAMADO".equals(existente.getEstadoOperativo())) {
            throw new ValidacionException("Solo se pueden editar viajes programados.");
        }
        viaje.setTipoViaje(existente.getTipoViaje()); // el tipo es inmodificable
        validarDatos(viaje);
        validarDisponibilidadEditar(viaje, existente, idSucursal);
        viajeDAO.actualizar(viaje);
    }

    public void eliminar(int id) throws SQLException, ValidacionException {
        Viaje v = viajeDAO.obtenerPorId(id);
        if (v == null) {
            throw new ValidacionException("El viaje no existe.");
        }
        if ("ALQUILER_PRIVADO".equals(v.getTipoViaje())) {
            throw new ValidacionException("No se puede eliminar un viaje de alquiler.");
        }
        if (!"PROGRAMADO".equals(v.getEstadoOperativo())) {
            throw new ValidacionException("Solo se elimina un viaje que no haya iniciado.");
        }
        if (boletoDAO.contarPorViaje(id) > 0) {
            throw new ValidacionException("No se puede eliminar: el viaje tiene boletos vendidos.");
        }
        viajeDAO.eliminar(id);
    }

    private void validarDatos(Viaje v) throws ValidacionException {
        if (!"REGULAR".equals(v.getTipoViaje()) && !"ALQUILER_PRIVADO".equals(v.getTipoViaje())) {
            throw new ValidacionException("Tipo de viaje no válido.");
        }
        if (v.getIdBus() <= 0) {
            throw new ValidacionException("Debe seleccionar un bus.");
        }
        if (v.getDpiChofer() == null || v.getDpiChofer().isEmpty()) {
            throw new ValidacionException("Debe seleccionar un chofer.");
        }
        if ("REGULAR".equals(v.getTipoViaje()) && v.getIdRuta() <= 0) {
            throw new ValidacionException("Un viaje regular requiere una ruta.");
        }
        LocalDateTime salida = parsear(v.getFechaHoraSalidaEstimada());
        LocalDateTime llegada = parsear(v.getFechaHoraLlegadaEstimada());
        if (!llegada.isAfter(salida)) {
            throw new ValidacionException("La llegada estimada debe ser posterior a la salida.");
        }
    }

    private void validarDisponibilidad(Viaje v, int idSucursal) throws SQLException, ValidacionException {
        String fecha = v.getFechaHoraSalidaEstimada().substring(0, 10);
        List<Bus> buses = busDAO.obtenerDisponibles(idSucursal, fecha);
        if (buses.stream().noneMatch(b -> b.getIdBus() == v.getIdBus())) {
            throw new ValidacionException("El bus seleccionado ya tiene un viaje el " + fecha + " o no está disponible.");
        }
        List<Chofer> choferes = choferDAO.obtenerDisponibles(idSucursal, fecha);
        if (choferes.stream().noneMatch(c -> c.getDpi().equals(v.getDpiChofer()))) {
            throw new ValidacionException("El chofer seleccionado ya tiene un viaje el " + fecha + " o no está disponible.");
        }
    }

    private void validarDisponibilidadEditar(Viaje nuevo, Viaje original, int idSucursal)
            throws SQLException, ValidacionException {
        String nuevaFecha = nuevo.getFechaHoraSalidaEstimada().substring(0, 10);
        String originalFecha = original.getFechaHoraSalidaEstimada().substring(0, 10);
        boolean mismaFecha = nuevaFecha.equals(originalFecha);

        if (nuevo.getIdBus() != original.getIdBus() || !mismaFecha) {
            List<Bus> buses = busDAO.obtenerDisponibles(idSucursal, nuevaFecha);
            if (buses.stream().noneMatch(b -> b.getIdBus() == nuevo.getIdBus())) {
                throw new ValidacionException("El bus seleccionado ya tiene un viaje el " + nuevaFecha + " o no está disponible.");
            }
        }
        boolean mismoChofer = nuevo.getDpiChofer() != null
                ? nuevo.getDpiChofer().equals(original.getDpiChofer()) : original.getDpiChofer() == null;
        if (!mismoChofer || !mismaFecha) {
            List<Chofer> choferes = choferDAO.obtenerDisponibles(idSucursal, nuevaFecha);
            if (choferes.stream().noneMatch(c -> c.getDpi().equals(nuevo.getDpiChofer()))) {
                throw new ValidacionException("El chofer seleccionado ya tiene un viaje el " + nuevaFecha + " o no está disponible.");
            }
        }
    }

    public Viaje obtenerConDetalles(int id) throws SQLException {
        return viajeDAO.obtenerPorIdConDetalles(id);
    }

    private LocalDateTime parsear(String fecha) throws ValidacionException {
        if (fecha == null || fecha.isEmpty()) {
            throw new ValidacionException("Las fechas de salida y llegada son obligatorias.");
        }
        try {
            return LocalDateTime.parse(fecha.replace("T", " "), FORMATO_DB);
        } catch (Exception e) {
            throw new ValidacionException("Formato de fecha inválido.");
        }
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }

    public void registrarSalida(int idViaje, int idSucursal, String fechaReal, double kmInicial)
            throws SQLException, ValidacionException {
        Viaje v = viajeDAO.obtenerPorId(idViaje);
        if (v == null) {
            throw new ValidacionException("El viaje no existe.");
        }
        if (!"PROGRAMADO".equals(v.getEstadoOperativo())) {
            throw new ValidacionException("Solo se registra la salida de viajes programados.");
        }
        Bus bus = busDAO.obtenerPorId(v.getIdBus());
        if (bus == null || bus.getIdSucursalActual() != idSucursal) {
            throw new ValidacionException("El viaje no pertenece a tu sucursal.");
        }
        if (fechaReal == null || fechaReal.isBlank()) {
            throw new ValidacionException("Indica la fecha y hora real de salida.");
        }
        if (kmInicial < 0) {
            throw new ValidacionException("El kilometraje debe ser mayor o igual a cero.");
        }
        viajeDAO.registrarSalida(idViaje, fechaReal, kmInicial);
        busDAO.actualizarDisponibilidad(v.getIdBus(), "Ocupado");
        if (v.getDpiChofer() != null && !v.getDpiChofer().isBlank()) {
            choferDAO.actualizarDisponibilidad(v.getDpiChofer(), "Ocupado");
        }
    }

    public void registrarLlegada(int idViaje, int idSucursal, String fechaReal, double kmFinal, double gastoCombustible)
            throws SQLException, ValidacionException {
        Viaje v = viajeDAO.obtenerPorId(idViaje);
        if (v == null) {
            throw new ValidacionException("El viaje no existe.");
        }
        if (!"EN_TRANSITO".equals(v.getEstadoOperativo())) {
            throw new ValidacionException("Solo se registra la llegada de viajes en tránsito.");
        }
        Bus bus = busDAO.obtenerPorId(v.getIdBus());
        if (bus == null || bus.getIdSucursalActual() != idSucursal) {
            throw new ValidacionException("El viaje no pertenece a tu sucursal.");
        }
        if (fechaReal == null || fechaReal.isBlank()) {
            throw new ValidacionException("Indica la fecha y hora real de llegada.");
        }
        if (kmFinal < v.getKilometrajeInicial()) {
            throw new ValidacionException("El kilometraje final no puede ser menor al inicial.");
        }
        if (gastoCombustible < 0) {
            throw new ValidacionException("El gasto de combustible debe ser mayor o igual a cero.");
        }
        double monto = configDAO.obtener();
        if (monto <= 0) {
            monto = 1.50;
        }
        double depreciacion = Math.round((kmFinal - v.getKilometrajeInicial()) * monto * 100.0) / 100.0;

        viajeDAO.registrarLlegada(idViaje, fechaReal, kmFinal, gastoCombustible, depreciacion);
        busDAO.actualizarKilometraje(v.getIdBus(), kmFinal);
        busDAO.actualizarDisponibilidad(v.getIdBus(), "Disponible");
        if (v.getDpiChofer() != null && !v.getDpiChofer().isBlank()) {
            choferDAO.actualizarDisponibilidad(v.getDpiChofer(), "Disponible");
        }
    }
}
