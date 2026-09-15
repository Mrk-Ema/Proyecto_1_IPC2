package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.AlquilerPrivado;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.AlquilerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvAlquiler", urlPatterns = {"/SvAlquiler"})
public class SvAlquiler extends HttpServlet {

    private final AlquilerService alquilerService = new AlquilerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("mis".equals(accion)) {
            mostrarMisAlquileres(request, response, null, null);
            return;
        }
        request.getRequestDispatcher("alquilerPrivado.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if ("pagar".equals(accion)) {
            pagar(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }

        // leer los campos del formulario
        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaSalida = request.getParameter("fechaSalida");
        String fechaRetorno = request.getParameter("fechaRetorno");
        int numeroPasajeros = 0;
        try {
            numeroPasajeros = Integer.parseInt(request.getParameter("numeroPasajeros"));
        } catch (NumberFormatException e) {
        }

        // aca se valida ls campos obligatorios
        if (origen == null || origen.isBlank() || destino == null || destino.isBlank()) {
            enviarError(request, response, "Origen y destino son obligatorios.");
            return;
        }
        if (origen.equalsIgnoreCase(destino)) {
            enviarError(request, response, "El origen y el destino deben ser distintos.");
            return;
        }
        if (fechaSalida == null || fechaSalida.isBlank()
                || fechaRetorno == null || fechaRetorno.isBlank()) {
            enviarError(request, response, "Indica fecha y hora de salida y de retorno (el alquiler es ida y vuelta).");
            return;
        }

        // calcular precio de pasajeros y fechas, y lo guarda en solicitar
        try {
            double precioEstimado = alquilerService.calcularPrecioEstimado(fechaSalida, fechaRetorno, numeroPasajeros);

            AlquilerPrivado a = new AlquilerPrivado();
            a.setDpiCliente(usuario.getDpi());
            a.setOrigen(origen.trim());
            a.setDestino(destino.trim());
            a.setFechaSalida(fechaSalida.replace('T', ' '));
            a.setFechaRetorno(fechaRetorno.replace('T', ' '));
            a.setNumeroPasajeros(numeroPasajeros);
            a.setPrecioEstimado(precioEstimado);

            int id = alquilerService.solicitar(a);

            request.setAttribute("idAlquiler", id);
            request.setAttribute("alquiler", a);
            request.getRequestDispatcher("alquilerSolicitudExito.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            enviarError(request, response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            enviarError(request, response, "Error del sistema al registrar la solicitud.");
        }
    }

    private void pagar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }
        int idAlquiler;
        try {
            idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
        } catch (NumberFormatException e) {
            mostrarMisAlquileres(request, response, "Solicitud inválida.", null);
            return;
        }
        String password = request.getParameter("password");

        AlquilerService.ResultadoPagar r = alquilerService.pagar(idAlquiler, usuario.getDpi(), password);
        switch (r.estado) {
            case OK -> {
                usuario.setSaldoCartera(r.nuevoSaldo);
                session.setAttribute("usuario", usuario);
                request.setAttribute("alquiler", r.alquiler);
                request.getRequestDispatcher("pagoAlquilerExito.jsp").forward(request, response);
            }
            case CONTRASENA_INCORRECTA ->
                mostrarMisAlquileres(request, response, "Contraseña incorrecta.", null);
            case SALDO_INSUFICIENTE ->
                mostrarMisAlquileres(request, response,
                        "Saldo insuficiente: te faltan Q" + String.format("%.2f", r.faltante), r.faltante);
            case NO_CONFIRMADO ->
                mostrarMisAlquileres(request, response,
                        "Esta solicitud aún no está confirmada por el administrador.", null);
            default ->
                mostrarMisAlquileres(request, response, "Error del sistema, intenta de nuevo.", null);
        }
    }

    private void mostrarMisAlquileres(HttpServletRequest request, HttpServletResponse response,
            String error, Double faltante) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }
        try {
            request.setAttribute("alquileres", alquilerService.misAlquileres(usuario.getDpi()));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("alquileres", new java.util.ArrayList<>());
        }
        request.setAttribute("error", error);
        request.setAttribute("faltante", faltante);
        request.getRequestDispatcher("misAlquileres.jsp").forward(request, response);
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("alquilerPrivado.jsp").forward(request, response);
    }
}