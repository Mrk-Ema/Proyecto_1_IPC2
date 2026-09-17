package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Chofer;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.ChoferService;
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
@WebServlet(name = "SvChofer", urlPatterns = {"/SvChofer"})
public class SvChofer extends HttpServlet {

    private final ChoferService choferService = new ChoferService();

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
                case "formEditar" -> {
                    Chofer c = choferService.obtenerPorDpi(request.getParameter("dpi"));
                    if (c == null) {
                        listar(request, response, null, "No se encontró el chofer solicitado.");
                        return;
                    }
                    request.setAttribute("chofer", c);
                    request.getRequestDispatcher("formularioChofer.jsp").forward(request, response);
                }
                default -> listar(request, response, null, null);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al consultar choferes", e);
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
                case "editar" -> {
                    Chofer c = crearChoferDesdeRequest(request);
                    choferService.actualizar(c,
                            request.getParameter("telefono"),
                            request.getParameter("direccion"));
                    listar(request, response, "Chofer actualizado correctamente.", null);
                }
                case "desactivar" -> {
                    choferService.desactivar(request.getParameter("dpi"));
                    listar(request, response, "Chofer desactivado.", null);
                }
                case "activar" -> {
                    choferService.activar(request.getParameter("dpi"));
                    listar(request, response, "Chofer activado.", null);
                }
                default -> listar(request, response, null, "Acción no válida.");
            }
        } catch (ChoferService.ValidacionException e) {
            if ("editar".equals(accion)) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("formularioChofer.jsp").forward(request, response);
            } else {
                listar(request, response, null, e.getMessage());
            }
        } catch (SQLException e) {
            throw new ServletException("Error al gestionar el chofer", e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Chofer> choferes = choferService.listarPorSucursal(sesionAdmin(request).getIdSucursalOrigen());
            request.setAttribute("choferes", choferes);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarChoferes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar choferes", e);
        }
    }

    private Chofer crearChoferDesdeRequest(HttpServletRequest request) {
        Chofer c = new Chofer();
        c.setDpi(request.getParameter("dpi"));
        c.setFoto(request.getParameter("foto"));
        c.setNumLicencia(request.getParameter("numLicencia"));
        c.setTipoLicencia(request.getParameter("tipoLicencia"));
        c.setFechaVencimientoLicencia(Date.valueOf(request.getParameter("fechaVencimientoLicencia")));
        c.setSalarioBaseViaje(Double.parseDouble(request.getParameter("salarioBaseViaje")));
        return c;
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