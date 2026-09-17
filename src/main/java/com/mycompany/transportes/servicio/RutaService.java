package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.RutaDAO;
import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.Ruta;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class RutaService {

    private final RutaDAO rutaDAO = new RutaDAO();
    private final ViajeDAO viajeDAO = new ViajeDAO();

    public List<Ruta> listarPorSucursal(int idSucursal) throws SQLException {
        return rutaDAO.obtenerPorSucursalConNombres(idSucursal);
    }

    public Ruta obtenerPorId(int id) throws SQLException {
        return rutaDAO.obtenerPorId(id);
    }

    public void crear(Ruta r) throws SQLException, ValidacionException {
        if (r.getIdSucursalOrigen() == r.getIdSucursalDestino()) {
            throw new ValidacionException("El origen y el destino no pueden ser la misma sucursal.");
        }
        if (r.getDistanciaKm() <= 0) {
            throw new ValidacionException("La distancia debe ser mayor que 0.");
        }
        if (r.getPrecioBoleto() <= 0) {
            throw new ValidacionException("El precio del boleto debe ser mayor que 0.");
        }
        if (rutaDAO.existeRuta(r.getIdSucursalOrigen(), r.getIdSucursalDestino())) {
            throw new ValidacionException("Ya existe una ruta entre esas dos sucursales.");
        }
        rutaDAO.crear(r);
    }

    public void eliminar(int id) throws SQLException, ValidacionException {
        if (viajeDAO.contarViajesDeRuta(id) > 0) {
            throw new ValidacionException("No se puede eliminar: la ruta tiene viajes asociados.");
        }
        rutaDAO.eliminar(id);
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }
}
