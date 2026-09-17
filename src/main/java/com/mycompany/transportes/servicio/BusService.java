package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.BusDAO;
import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.Bus;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class BusService {

    private final BusDAO busDAO = new BusDAO();
    private final ViajeDAO viajeDAO = new ViajeDAO();

    public List<Bus> listarPorSucursal(int idSucursal) throws SQLException {
        return busDAO.obtenerPorSucursalConViajesActivos(idSucursal);
    }

    public Bus obtenerPorId(int id) throws SQLException {
        return busDAO.obtenerPorId(id);
    }

    public void crear(Bus bus) throws SQLException, ValidacionException {
        normalizar(bus);
        validarDatos(bus);
        if (busDAO.obtenerPorPlaca(bus.getPlaca()) != null) {
            throw new ValidacionException("Ya existe un bus con la placa " + bus.getPlaca());
        }
        busDAO.crear(bus);
    }

    public void actualizar(Bus bus) throws SQLException, ValidacionException {
        normalizar(bus);
        validarDatos(bus);
        Bus existente = busDAO.obtenerPorPlaca(bus.getPlaca());
        if (existente != null && existente.getIdBus() != bus.getIdBus()) {
            throw new ValidacionException("Ya existe otro bus con la placa " + bus.getPlaca());
        }
        busDAO.actualizar(bus);
    }

    public void desactivar(int id) throws SQLException, ValidacionException {
        if (viajeDAO.contarViajesActivos(id) > 0) {
            throw new ValidacionException("No se puede desactivar: el bus tiene viajes programados o en tránsito.");
        }
        busDAO.desactivar(id);
    }

    public void activar(int id) throws SQLException {
        busDAO.activar(id);
    }

    private void normalizar(Bus bus) {
        if (bus.getPlaca() != null) {
            bus.setPlaca(bus.getPlaca().trim().toUpperCase());
        }
    }

    private void validarDatos(Bus bus) throws ValidacionException {
        if (bus.getPlaca() == null || bus.getPlaca().isEmpty()) {
            throw new ValidacionException("La placa es obligatoria.");
        }
        if (bus.getMarca() == null || bus.getMarca().trim().isEmpty()) {
            throw new ValidacionException("La marca es obligatoria.");
        }
        if (bus.getModelo() == null || bus.getModelo().trim().isEmpty()) {
            throw new ValidacionException("El modelo es obligatorio.");
        }
        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
        if (bus.getAñoFabricacion() < 1990 || bus.getAñoFabricacion() > añoActual + 1) {
            throw new ValidacionException("El año de fabricación debe estar entre 1990 y " + (añoActual + 1) + ".");
        }
        if (bus.getCapacidadPasajeros() < 1 || bus.getCapacidadPasajeros() > 120) {
            throw new ValidacionException("La capacidad debe estar entre 1 y 120 pasajeros.");
        }
        if (bus.getKilometrajeActual() < 0) {
            throw new ValidacionException("El kilometraje no puede ser menor que 0.");
        }
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }
}
