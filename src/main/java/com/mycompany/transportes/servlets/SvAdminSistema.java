package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.ConfiguracionSistemaDAO;
import com.mycompany.transportes.modelo.Sucursal;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.SucursalService;
import com.mycompany.transportes.servicio.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvAdminSistema", urlPatterns = {"/SvAdminSistema"})
public class SvAdminSistema extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final SucursalService sucursalService = new SucursalService();
    private final ConfiguracionSistemaDAO configDAO = new ConfiguracionSistemaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (sesionAdminSistema(request) == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String accion = request.getParameter("accion");
        try {
            if (null == accion) {
                listarAdmins(request, response, null, null);
            } else {
                switch (accion) {
                    case "listar" ->
                        listarAdmins(request, response, null, null);
                    case "formCrear" -> {
                        prepararForm(request, null);
                        request.getRequestDispatcher("formularioAdminSucursal.jsp").forward(request, response);
                    }
                    case "formEditar" -> {
                        Usuario u = usuarioService.obtenerPorDpi(request.getParameter("dpi"));
                        if (u == null) {
                            listarAdmins(request, response, null, "No se encontró el administrador.");
                            return;
                        }
                        prepararForm(request, u);
                        request.getRequestDispatcher("formularioAdminSucursal.jsp").forward(request, response);
                    }
                    case "formConfig" -> {
                        request.setAttribute("monto", configDAO.obtener());
                        request.getRequestDispatcher("configuracionSistema.jsp").forward(request, response);
                    }
                    case "usuarios" ->
                        listarUsuarios(request, response, null, null);
                    default ->
                        listarAdmins(request, response, null, "Acción no válida.");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error al consultar administradores", e);
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
                    Usuario u = crearAdminDesdeRequest(request, false);
                    usuarioService.registrarAdminSucursal(u);
                    listarAdmins(request, response, "Administrador creado correctamente.", null);
                }
                case "editar" -> {
                    Usuario u = crearAdminDesdeRequest(request, true);
                    usuarioService.actualizarAdminSucursal(u);
                    listarAdmins(request, response, "Administrador actualizado correctamente.", null);
                }
                case "desactivar" -> {
                    usuarioService.desactivarUsuario(request.getParameter("dpi"));
                    listarAdmins(request, response, "Administrador desactivado.", null);
                }
                case "activar" -> {
                    usuarioService.activarUsuario(request.getParameter("dpi"));
                    listarAdmins(request, response, "Administrador activado.", null);
                }
                case "desactivarUsuario" -> {
                    usuarioService.desactivarUsuario(request.getParameter("dpi"));
                    listarUsuarios(request, response, "Usuario desactivado.", null);
                }
                case "activarUsuario" -> {
                    usuarioService.activarUsuario(request.getParameter("dpi"));
                    listarUsuarios(request, response, "Usuario activado.", null);
                }
                case "guardarConfig" -> {
                    double monto = Double.parseDouble(request.getParameter("monto"));
                    if (monto <= 0) {
                        throw new IllegalArgumentException("El monto debe ser mayor a 0.");
                    }
                    configDAO.actualizar(monto);
                    response.sendRedirect("SvAdminSistema?accion=formConfig&guardado=ok");
                }
                default ->
                    listarAdmins(request, response, null, "Acción no válida.");
            }
        } catch (UsuarioService.ValidacionException | IllegalArgumentException e) {
            if (null == accion) {
                listarAdmins(request, response, null, e.getMessage());
            } else {
                switch (accion) {
                    case "crear", "editar" -> {
                        request.setAttribute("error", e.getMessage());
                        try {
                            prepararForm(request, "editar".equals(accion) ? crearAdminDesdeRequest(request, true) : null);
                        } catch (SQLException ex) {
                            throw new ServletException("Error al preparar formulario", ex);
                        }
                        request.getRequestDispatcher("formularioAdminSucursal.jsp").forward(request, response);
                    }
                    case "guardarConfig" -> {
                        request.setAttribute("error", e.getMessage());
                        request.setAttribute("monto", request.getParameter("monto"));
                        request.getRequestDispatcher("configuracionSistema.jsp").forward(request, response);
                    }
                    case "desactivarUsuario", "activarUsuario" ->
                        listarUsuarios(request, response, null, e.getMessage());
                    default ->
                        listarAdmins(request, response, null, e.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al procesar la solicitud", ex);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            Map<Integer, String> mapaSucursal = new HashMap<>();
            for (Sucursal s : sucursalService.listar()) {
                mapaSucursal.put(s.getIdSucursal(), s.getNombre());
            }
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("mapaSucursal", mapaSucursal);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarUsuarios.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar usuarios", e);
        }
    }

    private void listarAdmins(HttpServletRequest request, HttpServletResponse response, String msj, String error)
            throws ServletException, IOException {
        try {
            List<Usuario> admins = usuarioService.listarAdminsSucursal();
            Map<Integer, String> mapaSucursal = new HashMap<>();
            for (Sucursal s : sucursalService.listar()) {
                mapaSucursal.put(s.getIdSucursal(), s.getNombre());
            }
            request.setAttribute("admins", admins);
            request.setAttribute("mapaSucursal", mapaSucursal);
            if (msj != null) {
                request.setAttribute("msj", msj);
            }
            if (error != null) {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("gestionarAdminSucursal.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar administradores", e);
        }
    }

    private void prepararForm(HttpServletRequest request, Usuario admin) throws SQLException {
        request.setAttribute("admin", admin);
        request.setAttribute("sucursales", sucursalService.listar());
    }

    private Usuario crearAdminDesdeRequest(HttpServletRequest request, boolean esEdicion) {
        Usuario u = new Usuario(
                request.getParameter("dpi"),
                request.getParameter("nombreCompleto"),
                request.getParameter("nit"),
                request.getParameter("telefono"),
                request.getParameter("direccion"),
                request.getParameter("correo"),
                esEdicion ? "xxxxxx" : request.getParameter("password"),
                "ADMIN_SUCURSAL", 0.0);
        u.setIdSucursalOrigen(Integer.parseInt(request.getParameter("idSucursalOrigen")));
        return u;
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
