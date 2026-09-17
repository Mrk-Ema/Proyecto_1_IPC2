package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.BusDAO;
import com.mycompany.transportes.dao.ChoferDAO;
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
        } else if ("pendientes".equals(accion)) {
            mostrarPendientes(request, response, null);
            return;
        } else if ("pagados".equals(accion)) {
            mostrarPagados(request, response, null);
            return;
        } else if ("formAsignar".equals(accion)) {
            mostrarFormAsignar(request, response);
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
        } else if ("confirmar".equals(accion)) {
            confirmar(request, response);
            return;
        } else if ("rechazar".equals(accion)) {
            rechazar(request, response);
            return;
        } else if ("asignar".equals(accion)) {
            asignar(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("SvLogin?accion=sinRegistro");
            return;
        }

        // leer los campos del forumlario
        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaSalida = request.getParameter("fechaSalida");
        String fechaRetorno = request.getParameter("fechaRetorno");
        int numeroPasajeros = 0;
        try {
            numeroPasajeros = Integer.parseInt(request.getParameter("numeroPasajeros"));
        } catch (NumberFormatException e) {
        }

        // validar campos obligatorios
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

        // calcular precio asi como validar también pasajeros y fechas, y guardar como atributo
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

    private void confirmar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null || !"ADMIN_SUCURSAL".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            int idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
            double precioConfirmado = Double.parseDouble(request.getParameter("precioConfirmado"));

            if (precioConfirmado <= 0) {
                mostrarPendientes(request, response, "El precio debe ser mayor a cero.");
                return;
            }
            alquilerService.confirmarPrecio(idAlquiler, precioConfirmado);

            mostrarPendientes(request, response, "Alquiler #" + idAlquiler + " confirmado con éxito.");
        } catch (NumberFormatException e) {
            mostrarPendientes(request, response, "Valores numéricos inválidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarPendientes(request, response, "Error al confirmar el alquiler.");
        }
    }

    private void rechazar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null || !"ADMIN_SUCURSAL".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            int idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
            alquilerService.rechazarAlquiler(idAlquiler);
            mostrarPendientes(request, response, "Alquiler #" + idAlquiler + " rechazado.");
        } catch (NumberFormatException e) {
            mostrarPendientes(request, response, "ID de alquiler inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarPendientes(request, response, "Error al rechazar el alquiler.");
        }
    }

    private void mostrarPendientes(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null || !"ADMIN_SUCURSAL".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            request.setAttribute("pendientes", alquilerService.obtenerAlquileresPendientes());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("pendientes", new java.util.ArrayList<>());
        }
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("alquileresPendientes.jsp").forward(request, response);
    }

    private Usuario sesionAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");
        return (u != null && "ADMIN_SUCURSAL".equals(u.getRol())) ? u : null;
    }

    private void mostrarPagados(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        Usuario admin = sesionAdmin(request);
        if (admin == null) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            request.setAttribute("pagados", alquilerService.listarPagadosSinViaje());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("pagados", new java.util.ArrayList<>());
        }
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("alquileresPagados.jsp").forward(request, response);
    }

    private void mostrarFormAsignar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario admin = sesionAdmin(request);
        if (admin == null) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            int idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
            AlquilerPrivado a = alquilerService.buscarPorId(idAlquiler);
            if (a == null) {
                mostrarPagados(request, response, "El alquiler no existe.");
                return;
            }
            if (!"PAGADO".equals(a.getEstadoPago()) || a.getIdViaje() > 0) {
                mostrarPagados(request, response, "Este alquiler ya no está disponible para asignación.");
                return;
            }
            String fechaSalida = a.getFechaSalida().substring(0, 10);
            request.setAttribute("alquiler", a);
            request.setAttribute("buses", new BusDAO().obtenerDisponibles(admin.getIdSucursalOrigen(), fechaSalida));
            request.setAttribute("choferes", new ChoferDAO().obtenerDisponibles(admin.getIdSucursalOrigen(), fechaSalida));
            request.getRequestDispatcher("asignarAlquiler.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            mostrarPagados(request, response, "Identificador de alquiler inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarPagados(request, response, "Error al preparar la asignación.");
        }
    }

    private void asignar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario admin = sesionAdmin(request);
        if (admin == null) {
            response.sendRedirect("login.jsp?error=acceso_denegado");
            return;
        }
        try {
            int idAlquiler = Integer.parseInt(request.getParameter("idAlquiler"));
            int idBus = Integer.parseInt(request.getParameter("idBus"));
            String dpiChofer = request.getParameter("dpiChofer");
            if (dpiChofer == null || dpiChofer.isBlank()) {
                request.setAttribute("error", "Debe seleccionar un chofer.");
                mostrarFormAsignar(request, response);
                return;
            }
            int idViaje = alquilerService.asignarBusYChofer(idAlquiler, idBus, dpiChofer, admin.getIdSucursalOrigen());
            mostrarPagados(request, response, "Viaje #" + idViaje + " asignado al alquiler #" + idAlquiler + " con éxito.");
        } catch (NumberFormatException e) {
            mostrarPagados(request, response, "Valores inválidos en la asignación.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormAsignar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarPagados(request, response, "Error al asignar bus y chofer.");
        }
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("alquilerPrivado.jsp").forward(request, response);
    }
}
