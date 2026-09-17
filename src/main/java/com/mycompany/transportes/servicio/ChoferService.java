package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.ChoferDAO;
import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.Chofer;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
public class ChoferService {

    private final ChoferDAO choferDAO = new ChoferDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ViajeDAO viajeDAO = new ViajeDAO();

    public List<Chofer> listarPorSucursal(int idSucursal) throws SQLException {
        return choferDAO.obtenerPorSucursalConDetalles(idSucursal);
    }

    public Chofer obtenerPorDpi(String dpi) throws SQLException {
        return choferDAO.obtenerPorDpi(dpi);
    }

    public void actualizar(Chofer c, String telefono, String direccion) throws SQLException, ValidacionException {
        validarDatos(c, telefono, direccion);
        choferDAO.actualizar(c);
        usuarioDAO.actualizarTelefonoDireccion(c.getDpi(), telefono, direccion);
    }

    public void desactivar(String dpi) throws SQLException, ValidacionException {
        if (viajeDAO.contarViajesActivosDeChofer(dpi) > 0) {
            throw new ValidacionException("No se puede desactivar: el chofer tiene viajes programados o en tránsito.");
        }
        usuarioDAO.desactivar(dpi);
    }

    public void activar(String dpi) throws SQLException {
        usuarioDAO.activar(dpi);
    }

    private void validarDatos(Chofer c, String telefono, String direccion) throws ValidacionException {
        if (c.getNumLicencia() == null || c.getNumLicencia().trim().isEmpty()) {
            throw new ValidacionException("El número de licencia es obligatorio.");
        }
        if (!"A".equals(c.getTipoLicencia()) && !"B".equals(c.getTipoLicencia())) {
            throw new ValidacionException("El tipo de licencia debe ser A o B.");
        }
        if (c.getFechaVencimientoLicencia() == null
                || c.getFechaVencimientoLicencia().before(new Date())) {
            throw new ValidacionException("La fecha de vencimiento de licencia debe ser futura.");
        }
        if (c.getSalarioBaseViaje() < 0) {
            throw new ValidacionException("El salario base no puede ser menor que 0.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ValidacionException("El teléfono es obligatorio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ValidacionException("La dirección es obligatoria.");
        }
    }

    public static class ValidacionException extends Exception {

        public ValidacionException(String mensaje) {
            super(mensaje);
        }
    }
}
