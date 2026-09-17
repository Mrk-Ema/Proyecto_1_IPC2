package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Bus;
import com.mycompany.transportes.modelo.Mantenimiento;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.BusService;
import com.mycompany.transportes.servicio.MantenimientoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvMantenimiento", urlPatterns = {"/SvMantenimiento"})
public class SvMantenimiento extends HttpServlet {

    private final MantenimientoService mantenimientoService = new MantenimientoService();
    private final BusService busService = new BusService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdmin(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null || accion.isBlank()) {
            accion = "listar";
        }
        try {
            if ("formCrear".equals(accion)) {
                request.setAttribute("buses", busService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen()));
                request.getRequestDispatcher("registrarMantenimiento.jsp").forward(request, response);
            } else {
                listar(request, response, null, null);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al consultar mantenimientos", e);
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
            if ("crear".equals(accion)) {
                int idBus = Integer.parseInt(request.getParameter("idBus"));
                double manoObra = Double.parseDouble(request.getParameter("montoManoObra"));
                double repuestos = Double.parseDouble(request.getParameter("montoRepuestos"));
                String f = request.getParameter("fechaMantenimiento");
                Date fecha = (f != null && !f.isBlank()) ? Date.valueOf(f) : null;
                mantenimientoService.registrar(idBus, manoObra, repuestos, fecha,
                        request.getParameter("descripcion"), sesionAdmin(request).getIdSucursalOrigen());
                listar(request, response, "Mantenimiento registrado correctamente.", null);
            } else {
                listar(request, response, null, "Acción no válida.");
            }
        } catch (MantenimientoService.ValidacionException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("buses", busService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen()));
            } catch (SQLException ex) {
                throw new ServletException("Error al cargar buses", ex);
            }
            request.getRequestDispatcher("registrarMantenimiento.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al registrar mantenimiento", e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("mantenimientos", mantenimientoService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen()));
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("mantenimientos.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar mantenimientos", e);
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
}
