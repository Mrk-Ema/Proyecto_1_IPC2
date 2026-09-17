package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Bus;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.BusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvBus", urlPatterns = {"/SvBus"})
public class SvBus extends HttpServlet {

    private final BusService busService = new BusService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdmin(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        try {
            switch (accion) {
                case "formCrear" ->
                    request.getRequestDispatcher("formularioBus.jsp").forward(request, response);
                case "formEditar" -> {
                    Bus bus = busService.obtenerPorId(Integer.parseInt(request.getParameter("idBus")));
                    if (bus == null) {
                        listar(request, response, null, "No se encontró el bus solicitado.");
                        return;
                    }
                    request.setAttribute("bus", bus);
                    request.getRequestDispatcher("formularioBus.jsp").forward(request, response);
                }
                default ->
                    listar(request, response, null, null);
            }
        } catch (NumberFormatException e) {
            listar(request, response, null, "Identificador de bus inválido.");
        } catch (SQLException e) {
            throw new ServletException("Error al consultar buses", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdmin(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        try {
            switch (accion == null ? "" : accion) {
                case "crear" -> {
                    Bus bus = crearBusDesdeRequest(request, false);
                    bus.setIdSucursalOrigen(sesionAdmin(request).getIdSucursalOrigen());
                    bus.setIdSucursalActual(sesionAdmin(request).getIdSucursalOrigen());
                    busService.crear(bus);
                    listar(request, response, "Bus creado correctamente.", null);
                }
                case "editar" -> {
                    Bus bus = crearBusDesdeRequest(request, true);
                    busService.actualizar(bus);
                    listar(request, response, "Bus actualizado correctamente.", null);
                }
                case "desactivar" -> {
                    busService.desactivar(Integer.parseInt(request.getParameter("idBus")));
                    listar(request, response, "Bus desactivado.", null);
                }
                case "activar" -> {
                    busService.activar(Integer.parseInt(request.getParameter("idBus")));
                    listar(request, response, "Bus activado.", null);
                }
                default ->
                    listar(request, response, null, "Acción no válida.");
            }
        } catch (BusService.ValidacionException | IllegalArgumentException e) {
            if ("crear".equals(accion) || "editar".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("formularioBus.jsp").forward(request, response);
            } else {
                listar(request, response, null, e.getMessage());
            }
        } catch (SQLException e) {
            throw new ServletException("Error al gestionar el bus", e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Bus> buses = busService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen());
            request.setAttribute("buses", buses);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarBuses.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar buses", e);
        }
    }

    private Bus crearBusDesdeRequest(HttpServletRequest request, boolean conId) {
        Bus bus = new Bus();
        if (conId) {
            bus.setIdBus(Integer.parseInt(request.getParameter("idBus")));
        }
        bus.setPlaca(request.getParameter("placa"));
        bus.setFoto(request.getParameter("foto"));
        bus.setMarca(request.getParameter("marca"));
        bus.setModelo(request.getParameter("modelo"));
        bus.setAñoFabricacion(Integer.parseInt(request.getParameter("añoFabricacion")));
        bus.setCapacidadPasajeros(Integer.parseInt(request.getParameter("capacidadPasajeros")));
        double km = 0;
        String kmParam = request.getParameter("kilometrajeActual");
        if (kmParam != null && !kmParam.isEmpty()) {
            km = Double.parseDouble(kmParam);
        }
        bus.setKilometrajeActual(km);
        return bus;
    }

    private Usuario sesionAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");
        return (u != null && "ADMIN_SUCURSAL".equals(u.getRol())) ? u : null;
    }
}
