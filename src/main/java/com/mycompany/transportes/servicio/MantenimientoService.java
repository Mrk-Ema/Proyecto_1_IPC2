package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.BusDAO;
import com.mycompany.transportes.dao.MantenimientoTallerDAO;
import com.mycompany.transportes.modelo.Bus;
import com.mycompany.transportes.modelo.Mantenimiento;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class MantenimientoService {

    private final MantenimientoTallerDAO mantenimientoDAO = new MantenimientoTallerDAO();
    private final BusDAO busDAO = new BusDAO();

    public List<Mantenimiento> listarPorSucursal(int idSucursal) throws SQLException {
        return mantenimientoDAO.obtenerPorSucursalConPlaca(idSucursal);
    }

    public int registrar(int idBus, double montoManoObra, double montoRepuestos,
            Date fecha, String descripcion, int idSucursal)
            throws SQLException, ValidacionException {
        Bus bus = busDAO.obtenerPorId(idBus);
        if (bus == null) {
            throw new ValidacionException("El bus no existe.");
        }
        if (bus.getIdSucursalOrigen() != idSucursal) {
            throw new ValidacionException("El bus no pertenece a tu sucursal.");
        }
        if (montoManoObra < 0 || montoRepuestos < 0) {
            throw new ValidacionException("Los montos de mano de obra y repuestos no pueden ser negativos.");
        }
        if (fecha == null) {
            throw new ValidacionException("Indica la fecha del mantenimiento.");
        }
        Mantenimiento m = new Mantenimiento(idBus, montoManoObra, montoRepuestos, fecha, descripcion);
        return mantenimientoDAO.crear(m);
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }
}
