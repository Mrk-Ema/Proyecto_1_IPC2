package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.Chofer;
import com.mycompany.transportes.modelo.Usuario;
import com.mycompany.transportes.servicio.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "SvUsuario", urlPatterns = {"/SvUsuario"})
public class SvUsuario extends HttpServlet {

    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipoRegistro = request.getParameter("tipoRegistro");
        if (tipoRegistro == null) {
            tipoRegistro = "cliente";
        }

        try {
            switch (tipoRegistro) {
                case "admin_sucursal" -> {
                    Usuario nuevoAdminSucursal = crearUsuarioDesdeRequest(request, "ADMIN_SUCURSAL");
                    nuevoAdminSucursal.setIdSucursalOrigen(Integer.parseInt(request.getParameter("idSucursalOrigen")));
                    usuarioService.registrarAdminSucursal(nuevoAdminSucursal);
                    response.sendRedirect("crearAdmiSucursal.jsp?exito=admin_creado");
                }
                case "chofer" -> {
                    HttpSession session = request.getSession(false);
                    Usuario adminActual = (Usuario) session.getAttribute("usuario");
                    Usuario nuevoChoferUsuario = crearUsuarioDesdeRequest(request, "CHOFER");
                    nuevoChoferUsuario.setIdSucursalOrigen(adminActual.getIdSucursalOrigen());
                    Chofer nuevoChofer = crearChoferDesdeRequest(request);
                    usuarioService.registrarChofer(nuevoChoferUsuario, nuevoChofer);
                    response.sendRedirect("crearChofer.jsp?exito=chofer_creado");
                }
                default -> {
                    Usuario nuevoCliente = crearUsuarioDesdeRequest(request, "CLIENTE");
                    usuarioService.registrarCliente(nuevoCliente);
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", nuevoCliente);
                    session.removeAttribute("invitado");
                    response.sendRedirect("home.jsp?registro=exito");
                }
            }
        } catch (UsuarioService.ValidacionException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            String destino = switch (tipoRegistro) {
                case "admin_sucursal" ->
                    "crearAdmiSucursal.jsp";
                case "chofer" ->
                    "crearChofer.jsp";
                default ->
                    "registro.jsp";
            };
            request.getRequestDispatcher(destino).forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al procesar el registro de usuario", e);
        }
    }

    private Usuario crearUsuarioDesdeRequest(HttpServletRequest request, String rol) {
        String dpi = request.getParameter("dpi");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String nit = request.getParameter("nit");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        return new Usuario(dpi, nombreCompleto, nit, telefono, direccion, correo, password, rol, 0.0);
    }

    private Chofer crearChoferDesdeRequest(HttpServletRequest request) {
        String dpi = request.getParameter("dpi");
        String numLicencia = request.getParameter("numLicencia");
        String tipoLicencia = request.getParameter("tipoLicencia");
        Date fechaVencimiento = Date.valueOf(request.getParameter("fechaVencimientoLicencia"));
        double salarioBase = Double.parseDouble(request.getParameter("salarioBaseViaje"));
        String foto = request.getParameter("foto");

        return new Chofer(dpi, foto, numLicencia, tipoLicencia, fechaVencimiento, salarioBase);
    }
}
