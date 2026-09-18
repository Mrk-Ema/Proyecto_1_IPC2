<%-- 
    Document   : miPerfil
    Created on : 17 sept 2026, 14:55:39
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Usuario"%>
<%
    Usuario u = (Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mi Perfil - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Mi Perfil</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getParameter("exito") != null) {%>
            <p style="color:green;">Perfil actualizado correctamente.</p>
            <% } %>

            <% if (u != null) {%>
            <form action="SvUsuario" method="POST">
                <input type="hidden" name="accion" value="editarPerfil">
                <input type="hidden" name="dpi" value="<%= u.getDpi()%>">

                <p><label>DPI: </label>
                    <input type="text" value="<%= u.getDpi()%>" readonly></p>

                <p><label>Nombre completo: </label>
                    <input type="text" name="nombreCompleto"
                           value="<%= request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto") : u.getNombreCompleto()%>" required></p>

                <p><label>Correo: </label>
                    <input type="text" value="<%= u.getCorreo()%>" readonly></p>

                <p><label>NIT: </label>
                    <input type="text" name="nit" value="<%= request.getParameter("nit") != null ? request.getParameter("nit") : u.getNit()%>" required></p>

                <p><label>Teléfono: </label>
                    <input type="text" name="telefono" maxlength="15"
                           value="<%= request.getParameter("telefono") != null ? request.getParameter("telefono") : u.getTelefono()%>" required></p>

                <p><label>Dirección: </label>
                    <input type="text" name="direccion"
                           value="<%= request.getParameter("direccion") != null ? request.getParameter("direccion") : u.getDireccion()%>" required></p>

                <button type="submit">Guardar Cambios</button>
            </form>
            <% } else { %>
            <p><a href="login.jsp">Inicia sesión para ver tu perfil.</a></p>
            <% }%>
        </div>
    </body>
</html>