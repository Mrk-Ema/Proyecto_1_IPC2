package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "SvLogin", urlPatterns = {"/SvLogin"})
public class SvLogin extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            if (accion.equals("logout")) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect("login.jsp");
                return;
            } else if (accion.equals("invitado")) {
                HttpSession session = request.getSession();
                session.setAttribute("invitado", true);
                response.sendRedirect("home.jsp");
                return;
            }
        }
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        try {
            Usuario usuario = usuarioDAO.obtenerPorCorreo(correo);
            if (usuario != null && usuario.getPassword().equals(password) && usuario.isEstado()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.removeAttribute("invitado");
                response.sendRedirect("home.jsp");
            } else {
                request.setAttribute("error", "Correo, contraseña incorrectos o usuario inactivo.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar el inicio de sesión", e);
        }
    }
}
