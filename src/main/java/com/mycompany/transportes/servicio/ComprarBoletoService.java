package com.mycompany.transportes.servicio;

import com.mycompany.transportes.conexion.Conexion;
import com.mycompany.transportes.dao.BoletoDAO;
import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.Boleto;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.modelo.ViajeDisponible;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ComprarBoletoService {

    private final ViajeDAO viajeDAO = new ViajeDAO();
    private final BoletoDAO boletoDAO = new BoletoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public enum EstadoPago {
        OK, CONTRASENA_INCORRECTA, ASIENTOS_TOMADOS, SALDO_INSUFICIENTE, ERROR_SISTEMA
    }

    public static class ResultadoPago {
        public EstadoPago estado;
        public ViajeDisponible viaje;
        public List<Integer> asientos = new ArrayList<>();
        public double total;
        public double faltante;
        public double nuevoSaldo;
    }

    public ResultadoPago procesarCompra(String dpi, int idViaje,
            List<Integer> asientos, String password) throws Exception {

        ResultadoPago r = new ResultadoPago();
        r.asientos = asientos;

        //Usuario y viaje FRESCOS de la BD
        Usuario usuario = usuarioDAO.obtenerPorDpi(dpi);
        r.viaje = viajeDAO.obtenerDisponibleDetalladoPorId(idViaje);

        //Contraseña 
        if (usuario == null || r.viaje == null) {
            r.estado = EstadoPago.ERROR_SISTEMA;
            return r;
        }
        if (!usuario.getPassword().equals(password)) {
            r.estado = EstadoPago.CONTRASENA_INCORRECTA;
            return r;
        }

        //Re-verificar asientos: si alguno se ocupó, cancelar
        for (Integer a : asientos) {
            if (r.viaje.getAsientosOcupados().contains(a)) {
                r.estado = EstadoPago.ASIENTOS_TOMADOS;
                return r;
            }
        }

        //Total y saldo
        r.total = r.viaje.getPrecioBoleto() * asientos.size();
        if (usuario.getSaldoCartera() < r.total) {
            r.faltante = r.total - usuario.getSaldoCartera();
            r.estado = EstadoPago.SALDO_INSUFICIENTE;
            return r;
        }

        //Transacción: descontar saldo y crear cada boleto
        r.nuevoSaldo = usuario.getSaldoCartera() - r.total;
        Connection conn = Conexion.obtener();
        try {
            conn.setAutoCommit(false);
            usuarioDAO.actualizarSaldo(conn, dpi, r.nuevoSaldo);
            for (Integer a : asientos) {
                boletoDAO.crear(conn, new Boleto(idViaje, dpi, a, r.viaje.getPrecioBoleto()));
            }
            conn.commit();
            r.estado = EstadoPago.OK;
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
            r.estado = EstadoPago.ERROR_SISTEMA;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
        return r;
    }
}