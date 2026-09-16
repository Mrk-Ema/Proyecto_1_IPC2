package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.modelo.ViajeDisponible;
import com.mycompany.transportes.servicio.ComprarBoletoService;
import com.mycompany.transportes.servicio.ComprarBoletoService.ResultadoPago;
import com.mycompany.transportes.servicio.ViajeRegularService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvBoleto", urlPatterns = {"/SvBoleto"})
public class SvBoleto extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if ("pagar".equals(accion)) {
            pagar(request, response);
        } else {
            preparar(request, response);
        }
    }

    // viene de la pagina de asientos y ahora muestra el resumen de pago
    private void preparar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }

        int idViaje = Integer.parseInt(request.getParameter("idViaje"));
        List<Integer> asientos = leerAsientos(request);

        try {
            ViajeDisponible viaje = new ViajeRegularService().obtenerViajeDisponiblePorId(idViaje);
            if (viaje == null) {
                response.sendRedirect("SvViajeRegular?accion=asientos&idViaje=" + idViaje + "&error=no_disponible");
                return;
            }
            // Re-verificar si un asiento ya se ocupo, mandar de vuelta actualizado
            for (Integer a : asientos) {
                if (viaje.getAsientosOcupados().contains(a)) {
                    response.sendRedirect("SvViajeRegular?accion=asientos&idViaje=" + idViaje + "&error=asientos_tomados");
                    return;
                }
            }
            double total = viaje.getPrecioBoleto() * asientos.size();
            request.setAttribute("viaje", viaje);
            request.setAttribute("asientos", asientos);
            request.setAttribute("total", total);
            request.setAttribute("saldo", usuario.getSaldoCartera());
            request.getRequestDispatcher("pagarBoleto.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("SvViajeRegular?accion=asientos&idViaje=" + idViaje + "&error=id_invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("SvViajeRegular?error=sistema");
        }
    }

    // viene de pagarBoleto y ahora procesa la compra
    private void pagar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }

        int idViaje = Integer.parseInt(request.getParameter("idViaje"));
        List<Integer> asientos = leerAsientos(request);
        String password = request.getParameter("password");

        try {
            ResultadoPago r = new ComprarBoletoService()
                    .procesarCompra(usuario.getDpi(), idViaje, asientos, password);

            switch (r.estado) {
                case OK -> {
                    usuario.setSaldoCartera(r.nuevoSaldo);
                    session.setAttribute("usuario", usuario);
                    request.setAttribute("viaje", r.viaje);
                    request.setAttribute("asientos", r.asientos);
                    request.setAttribute("total", r.total);
                    request.getRequestDispatcher("compraExitosa.jsp").forward(request, response);
                }
                case ASIENTOS_TOMADOS ->
                    response.sendRedirect("SvViajeRegular?accion=asientos&idViaje=" + idViaje + "&error=asientos_tomados");
                case CONTRASENA_INCORRECTA -> {
                    request.setAttribute("error", "Contraseña incorrecta.");
                    repetirPago(request, response, idViaje, asientos);
                }
                case SALDO_INSUFICIENTE -> {
                    request.setAttribute("error", "Saldo insuficiente.");
                    request.setAttribute("faltante", r.faltante);
                    repetirPago(request, response, idViaje, asientos);
                }
                default -> {
                    request.setAttribute("error", "Error del sistema, intenta de nuevo.");
                    repetirPago(request, response, idViaje, asientos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("SvViajeRegular?error=sistema");
        }
    }

    // Recarga los atributos y vuelve a mostrar pagarBoleto.jsp
    private void repetirPago(HttpServletRequest request, HttpServletResponse response,
            int idViaje, List<Integer> asientos) throws ServletException, IOException {
        try {
            ViajeDisponible viaje = new ViajeRegularService().obtenerViajeDisponiblePorId(idViaje);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            request.setAttribute("viaje", viaje);
            request.setAttribute("asientos", asientos);
            request.setAttribute("total", viaje.getPrecioBoleto() * asientos.size());
            request.setAttribute("saldo", usuario.getSaldoCartera());
            request.getRequestDispatcher("pagarBoleto.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("SvViajeRegular?error=sistema");
        }
    }

    private List<Integer> leerAsientos(HttpServletRequest request) {
        List<Integer> lista = new ArrayList<>();
        String[] valores = request.getParameterValues("asientos");
        if (valores != null) {
            for (String s : valores) {
                lista.add(Integer.parseInt(s));
            }
        }
        return lista;
    }
}
