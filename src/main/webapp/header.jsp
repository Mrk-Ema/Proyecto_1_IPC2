<%-- 
    Document   : header
    Created on : 10 sept 2026, 3:00:25
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Usuario"%>
<%
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
%>
<div class="container">
    <header class="d-flex flex-wrap justify-content-between align-items-center py-3 mb-4 border-bottom">
        <a href="home.jsp" class="fs-4 text-decoration-none link-body-emphasis mb-2 mb-md-0">Transportes MRK</a>
        <nav class="d-flex flex-wrap align-items-center gap-2">
            <% if (usuarioActual == null) { %>
            <a href="SvLogin?accion=sinRegistro" class="btn btn-outline-primary btn-sm">Comprar Boleto</a>
            <% } else {%>
            <a href="SvViajeRegular" class="btn btn-outline-primary btn-sm">Comprar Boletos</a>
            <a href="SvAlquiler" class="btn btn-outline-primary btn-sm">Alquilar Bus</a>
            <a href="SvAlquiler?accion=mis" class="btn btn-outline-primary btn-sm">Mis Alquileres</a>
            <% }%>
            <a href="servicios.jsp" class="btn btn-outline-secondary btn-sm">Servicios</a>
            <a href="quienesSomos.jsp" class="btn btn-outline-secondary btn-sm">Quiénes Somos</a>
            <% if (usuarioActual == null) { %>
            <a href="login.jsp" class="btn btn-outline-success btn-sm">Iniciar Sesión</a>
            <% } else {%>
            <a href="miPerfil.jsp" class="btn btn-outline-secondary btn-sm">Mi Perfil</a>
            <span class="text-secondary">Hola, <%= usuarioActual.getNombreCompleto()%></span>
            <% if (usuarioActual.getRol().equals("CLIENTE")) {%>
            <span class="text-success">Saldo: Q<%= String.format("%.2f", usuarioActual.getSaldoCartera())%></span>
            <a href="recargar.jsp" class="btn btn-outline-warning btn-sm">Recargar</a>
            <% } %>
            <% if (usuarioActual.getRol().equals("ADMIN_SISTEMA")) { %>
            <a href="panelAdminSistema.jsp" class="btn btn-outline-dark btn-sm">Desarrollador</a>
            <% } else if (usuarioActual.getRol().equals("ADMIN_SUCURSAL")) { %>
            <a href="panelAdminSucursal.jsp" class="btn btn-outline-dark btn-sm">Control</a>
            <% } else if (usuarioActual.getRol().equals("CHOFER")) { %>
            <a href="panelChofer.jsp" class="btn btn-outline-dark btn-sm">Mis Viajes</a>
            <% } %>
            <a href="SvLogin?accion=logout" class="btn btn-link btn-sm">Cerrar Sesión</a>
            <% }%>
        </nav>
    </header>
</div>
