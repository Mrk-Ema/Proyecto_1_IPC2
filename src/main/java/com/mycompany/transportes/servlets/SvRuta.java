package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.SucursalDAO;
import com.mycompany.transportes.modelo.Ruta;
import com.mycompany.transportes.modelo.Sucursal;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.RutaService;
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
@WebServlet(name = "SvRuta", urlPatterns = {"/SvRuta"})
public class SvRuta extends HttpServlet {

    private final RutaService rutaService = new RutaService();
    private final SucursalDAO sucursalDAO = new SucursalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario admin = sesionAdmin(request);
        if (admin == null) {
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
                    prepararFormulario(request, admin);
                    request.getRequestDispatcher("formularioRuta.jsp").forward(request, response);
                }
                default ->
                    listar(request, response, null, null);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al consultar rutas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario admin = sesionAdmin(request);
        if (admin == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        try {
            switch (accion == null ? "" : accion) {
                case "crear" -> {
                    rutaService.crear(crearRutaDesdeRequest(request, admin));
                    listar(request, response, "Ruta creada correctamente.", null);
                }
                case "eliminar" -> {
                    rutaService.eliminar(Integer.parseInt(request.getParameter("idRuta")));
                    listar(request, response, "Ruta eliminada.", null);
                }
                default ->
                    listar(request, response, null, "Acción no válida.");
            }
        } catch (RutaService.ValidacionException | NumberFormatException e) {
            if ("crear".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                try {
                    prepararFormulario(request, admin);
                } catch (SQLException ex) {
                    throw new ServletException("Error al preparar el formulario", ex);
                }
                request.getRequestDispatcher("formularioRuta.jsp").forward(request, response);
            } else {
                listar(request, response, null, e.getMessage());
            }
        } catch (SQLException e) {
            throw new ServletException("Error al gestionar la ruta", e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Ruta> rutas = rutaService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen());
            request.setAttribute("rutas", rutas);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarRutas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar rutas", e);
        }
    }

    private void prepararFormulario(HttpServletRequest request, Usuario admin) throws SQLException {
        List<Sucursal> sucursales = sucursalDAO.obtenerTodas();
        request.setAttribute("sucursales", sucursales);
        request.setAttribute("idSucursalOrigen", admin.getIdSucursalOrigen());
        Sucursal origen = sucursalDAO.obtenerPorId(admin.getIdSucursalOrigen());
        request.setAttribute("nombreOrigen", origen != null ? origen.getNombre() : "");
    }

    private Ruta crearRutaDesdeRequest(HttpServletRequest request, Usuario admin) {
        Ruta r = new Ruta();
        r.setIdSucursalOrigen(admin.getIdSucursalOrigen());
        r.setIdSucursalDestino(Integer.parseInt(request.getParameter("idSucursalDestino")));
        r.setDistanciaKm(Double.parseDouble(request.getParameter("distanciaKm")));
        r.setPrecioBoleto(Double.parseDouble(request.getParameter("precioBoleto")));
        return r;
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
