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
<nav>
    
    <a href="home.jsp">Transportes MRK</a>
    <a href="servicios.jsp">Servicios</a>
    <a href="quienesSomos.jsp">Quiénes Somos</a>

    <% if (usuarioActual == null) { %>
    <a href="login.jsp">Iniciar Sesión</a>
    <% } else {%>
    <span>Hola, <%= usuarioActual.getNombreCompleto()%></span>
    <% if (usuarioActual.getRol().equals("ADMIN_SISTEMA")) { %>
    <a href="panelAdminSistema.jsp">Desarrollador</a>
    <% } else if (usuarioActual.getRol().equals("ADMIN_SUCURSAL")) { %>
    <a href="panelAdminSucursal.jsp">Control</a>
    <% } else if (usuarioActual.getRol().equals("CHOFER")) { %>
    <a href="panelChofer.jsp">Mis Viajes</a>
    <% } %>
    <a href="SvLogin?accion=logout">Cerrar Sesión</a>
    <% }%>
</nav>
