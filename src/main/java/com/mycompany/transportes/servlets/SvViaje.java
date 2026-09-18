package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.BusDAO;
import com.mycompany.transportes.dao.ChoferDAO;
import com.mycompany.transportes.dao.RutaDAO;
import com.mycompany.transportes.modelo.Bus;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.modelo.Viaje;
import com.mycompany.transportes.servicio.ViajeService;
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
@WebServlet(name = "SvViaje", urlPatterns = {"/SvViaje"})
public class SvViaje extends HttpServlet {

    private final ViajeService viajeService = new ViajeService();

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
                case "formCrear" -> {
                    prepararFormulario(request);
                    request.getRequestDispatcher("formularioViaje.jsp").forward(request, response);
                }
                case "formEditar" -> {
                    Viaje viaje = viajeService.obtenerPorId(Integer.parseInt(request.getParameter("idViaje")));
                    if (viaje == null) {
                        listar(request, response, null, "No se encontró el viaje solicitado.");
                        return;
                    }
                    prepararFormulario(request);
                    request.setAttribute("viaje", viaje);
                    request.getRequestDispatcher("formularioViaje.jsp").forward(request, response);
                }
                case "formSalida" -> {
                    prepararFormSalida(request);
                    request.getRequestDispatcher("registrarSalida.jsp").forward(request, response);
                }
                case "formLlegada" -> {
                    prepararFormLlegada(request);
                    request.getRequestDispatcher("registrarLlegada.jsp").forward(request, response);
                }
                default ->
                    listar(request, response, null, null);
            }
        } catch (NumberFormatException e) {
            listar(request, response, null, "Identificador de viaje inválido.");
        } catch (SQLException e) {
            throw new ServletException("Error al consultar viajes", e);
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
                    Viaje viaje = crearViajeDesdeRequest(request, false);
                    viajeService.crear(viaje, sesionAdmin(request).getIdSucursalOrigen());
                    listar(request, response, "Viaje creado correctamente.", null);
                }
                case "editar" -> {
                    Viaje viaje = crearViajeDesdeRequest(request, true);
                    viajeService.actualizar(viaje, sesionAdmin(request).getIdSucursalOrigen());
                    listar(request, response, "Viaje actualizado correctamente.", null);
                }
                case "eliminar" -> {
                    viajeService.eliminar(Integer.parseInt(request.getParameter("idViaje")));
                    listar(request, response, "Viaje eliminado.", null);
                }
                case "salida" -> {
                    int idViaje = Integer.parseInt(request.getParameter("idViaje"));
                    String fechaReal = formatearFecha(request.getParameter("fechaHoraSalidaReal"));
                    double kmInicial = Double.parseDouble(request.getParameter("kmInicial"));
                    viajeService.registrarSalida(idViaje, sesionAdmin(request).getIdSucursalOrigen(), fechaReal, kmInicial);
                    listar(request, response, "Salida registrada. El viaje está en tránsito.", null);
                }
                case "llegada" -> {
                    int idViaje = Integer.parseInt(request.getParameter("idViaje"));
                    String fechaReal = formatearFecha(request.getParameter("fechaHoraLlegadaReal"));
                    double kmFinal = Double.parseDouble(request.getParameter("kmFinal"));
                    double gastoCombustible = Double.parseDouble(request.getParameter("gastoCombustible"));
                    viajeService.registrarLlegada(idViaje, sesionAdmin(request).getIdSucursalOrigen(), fechaReal, kmFinal, gastoCombustible);
                    listar(request, response, "Llegada registrada. El viaje está finalizado.", null);
                }
                default ->
                    listar(request, response, null, "Acción no válida.");
            }
        } catch (ViajeService.ValidacionException | IllegalArgumentException e) {
            if ("crear".equals(accion) || "editar".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                request.setAttribute("viaje", crearViajeDesdeRequest(request, "editar".equals(accion)));
                try {
                    prepararFormulario(request);
                } catch (SQLException ex) {
                    throw new ServletException("Error al preparar fomrulario", ex);
                }
                request.getRequestDispatcher("formularioViaje.jsp").forward(request, response);
            } else if ("salida".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                try {
                    prepararFormSalida(request);
                } catch (SQLException ex) {
                    throw new ServletException("Error al preparar formulario", ex);
                }
                request.getRequestDispatcher("registrarSalida.jsp").forward(request, response);
            } else if ("llegada".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                try {
                    prepararFormLlegada(request);
                } catch (SQLException ex) {
                    throw new ServletException("Error al preparar formulario", ex);
                }
                request.getRequestDispatcher("registrarLlegada.jsp").forward(request, response);
            } else {
                listar(request, response, null, e.getMessage());
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al procesar la solicitud", ex);
        }
    }

    private void prepararFormulario(HttpServletRequest request) throws SQLException {
        Usuario admin = sesionAdmin(request);
        request.setAttribute("buses", new BusDAO().obtenerPorSucursalConViajesActivos(admin.getIdSucursalOrigen()));
        request.setAttribute("choferes", new ChoferDAO().obtenerPorSucursalConDetalles(admin.getIdSucursalOrigen()));
        request.setAttribute("rutas", new RutaDAO().obtenerDisponiblesConNombres(admin.getIdSucursalOrigen()));
    }

    private Viaje crearViajeDesdeRequest(HttpServletRequest request, boolean conId) {
        Viaje v = new Viaje();
        if (conId) {
            v.setIdViaje(Integer.parseInt(request.getParameter("idViaje")));
        }
        String idBus = request.getParameter("idBus");
        v.setIdBus(idBus != null && !idBus.isEmpty() ? Integer.parseInt(idBus) : 0);
        v.setDpiChofer(request.getParameter("dpiChofer"));
        String idRuta = request.getParameter("idRuta");
        v.setIdRuta(idRuta != null && !idRuta.isEmpty() ? Integer.parseInt(idRuta) : 0);
        v.setTipoViaje("REGULAR");
        v.setFechaHoraSalidaEstimada(formatearFecha(request.getParameter("fechaHoraSalidaEstimada")));
        v.setFechaHoraLlegadaEstimada(formatearFecha(request.getParameter("fechaHoraLlegadaEstimada")));
        return v;
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return null;
        }
        return fecha.replace("T", " ") + (fecha.length() == 16 ? ":00" : "");
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Viaje> viajes = viajeService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen());
            request.setAttribute("viajes", viajes);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarViajes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar viajes", e);
        }
    }

    private Usuario sesionAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");
        return (u != null && "ADMIN_SUCURSAL".equals(u.getRol())) ? u : null;
    }

    private void prepararFormSalida(HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("idViaje"));
        Viaje viaje = viajeService.obtenerConDetalles(id);
        if (viaje == null) {
            throw new SQLException("El viaje no existe.");
        }
        request.setAttribute("viaje", viaje);
        Bus bus = viaje.getIdBus() > 0 ? new BusDAO().obtenerPorId(viaje.getIdBus()) : null;
        request.setAttribute("kmActual", bus != null ? bus.getKilometrajeActual() : 0.0);
        String f = request.getParameter("fechaHoraSalidaReal");
        if (f != null && !f.isBlank()) {
            request.setAttribute("fechaActual", f.replace(" ", "T").substring(0, 16));
        } else {
            request.setAttribute("fechaActual", java.time.LocalDateTime.now().toString().substring(0, 16));
        }
    }

    private void prepararFormLlegada(HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("idViaje"));
        Viaje viaje = viajeService.obtenerConDetalles(id);
        if (viaje == null) {
            throw new SQLException("El viaje no existe.");
        }
        request.setAttribute("viaje", viaje);
        Bus bus = viaje.getIdBus() > 0 ? new BusDAO().obtenerPorId(viaje.getIdBus()) : null;
        request.setAttribute("kmBus", bus != null ? bus.getKilometrajeActual() : 0.0);
        String f = request.getParameter("fechaHoraLlegadaReal");
        if (f != null && !f.isBlank()) {
            request.setAttribute("fechaActual", f.replace(" ", "T").substring(0, 16));
        } else {
            request.setAttribute("fechaActual", java.time.LocalDateTime.now().toString().substring(0, 16));
        }
    }
}
