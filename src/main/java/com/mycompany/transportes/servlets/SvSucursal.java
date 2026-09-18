package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Sucursal;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.SucursalService;
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
@WebServlet(name = "SvSucursal", urlPatterns = {"/SvSucursal"})
public class SvSucursal extends HttpServlet {

    private final SucursalService sucursalService = new SucursalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdminSistema(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        try {
            if (accion == null || "listar".equals(accion)) {
                listar(request, response, null, null);
            } else if ("formCrear".equals(accion)) {
                request.getRequestDispatcher("formularioSucursal.jsp").forward(request, response);
            } else if ("formEditar".equals(accion)) {
                Sucursal s = sucursalService.obtenerPorId(Integer.parseInt(request.getParameter("idSucursal")));
                if (s == null) {
                    listar(request, response, null, "No se encontró la sucursal.");
                    return;
                }
                request.setAttribute("sucursal", s);
                request.getRequestDispatcher("formularioSucursal.jsp").forward(request, response);
            } else {
                listar(request, response, null, "Acción no válida.");
            }
        } catch (NumberFormatException e) {
            listar(request, response, null, "Identificador inválido.");
        } catch (SQLException e) {
            throw new ServletException("Error al consultar sucursales", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdminSistema(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        try {
            switch (accion == null ? "" : accion) {
                case "crear" -> {
                    sucursalService.crear(crearSucursalDesdeRequest(request, false));
                    listar(request, response, "Sucursal creada correctamente.", null);
                }
                case "editar" -> {
                    sucursalService.actualizar(crearSucursalDesdeRequest(request, true));
                    listar(request, response, "Sucursal actualizada correctamente.", null);
                }
                case "desactivar" -> {
                    sucursalService.desactivar(Integer.parseInt(request.getParameter("idSucursal")));
                    listar(request, response, "Sucursal desactivada.", null);
                }
                case "activar" -> {
                    sucursalService.activar(Integer.parseInt(request.getParameter("idSucursal")));
                    listar(request, response, "Sucursal activada.", null);
                }
                default ->
                    listar(request, response, null, "Acción no válida.");
            }
        } catch (SucursalService.ValidacionException | IllegalArgumentException e) {
            if ("crear".equals(accion) || "editar".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("formularioSucursal.jsp").forward(request, response);
            } else {
                listar(request, response, null, e.getMessage());
            }
        } catch (SQLException e) {
            throw new ServletException("Error al gestionar sucursales", e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("sucursales", sucursalService.listar());
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarSucursales.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar sucursales", e);
        }
    }

    private Sucursal crearSucursalDesdeRequest(HttpServletRequest request, boolean conId) {
        Sucursal s = new Sucursal();
        if (conId) {
            s.setIdSucursal(Integer.parseInt(request.getParameter("idSucursal")));
        }
        s.setNombre(request.getParameter("nombre"));
        s.setDireccion(request.getParameter("direccion"));
        s.setTelefono(request.getParameter("telefono"));
        return s;
    }

    private Usuario sesionAdminSistema(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");
        return (u != null && "ADMIN_SISTEMA".equals(u.getRol())) ? u : null;
    }
}
