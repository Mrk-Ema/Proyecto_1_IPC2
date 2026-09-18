package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.SucursalDAO;
import com.mycompany.transportes.modelo.Sucursal;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class SucursalService {

    private final SucursalDAO sucursalDAO = new SucursalDAO();

    public List<Sucursal> listar() throws SQLException {
        return sucursalDAO.obtenerTodas();
    }

    public Sucursal obtenerPorId(int id) throws SQLException {
        return sucursalDAO.obtenerPorId(id);
    }

    public void crear(Sucursal s) throws SQLException, ValidacionException {
        validarCampos(s);
        for (Sucursal existente : sucursalDAO.obtenerTodas()) {
            if (existente.getNombre().equalsIgnoreCase(s.getNombre())) {
                throw new ValidacionException("Ya existe una sucursal con ese nombre.");
            }
        }
        sucursalDAO.crear(s);
    }

    public void actualizar(Sucursal s) throws SQLException, ValidacionException {
        validarCampos(s);
        for (Sucursal existente : sucursalDAO.obtenerTodas()) {
            if (existente.getIdSucursal() != s.getIdSucursal()
                    && existente.getNombre().equalsIgnoreCase(s.getNombre())) {
                throw new ValidacionException("Ya existe una sucursal con ese nombre.");
            }
        }
        sucursalDAO.actualizar(s);
    }

    public void desactivar(int id) throws SQLException {
        sucursalDAO.desactivar(id);
    }

    public void activar(int id) throws SQLException {
        sucursalDAO.activar(id);
    }

    private void validarCampos(Sucursal s) throws ValidacionException {
        if (s.getNombre() == null || s.getNombre().isBlank()) {
            throw new ValidacionException("El nombre de la sucursal es obligatorio.");
        }
        if (s.getDireccion() == null || s.getDireccion().isBlank()) {
            throw new ValidacionException("La dirección es obligatoria.");
        }
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }
}
