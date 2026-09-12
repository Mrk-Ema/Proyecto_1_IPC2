<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear Admin de Sucursal</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <h1>Crear Administrador de Sucursal</h1>

        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>

        <form action="SvUsuario" method="POST">
            <input type="hidden" name="tipoRegistro" value="admin_sucursal">
            <p><label>DPI: </label>
                <input type="text" name="dpi" value="<%= request.getParameter("dpi") != null ? request.getParameter("dpi") : "" %>"></p>
            <p><label>Nombre completo: </label>
                <input type="text" name="nombreCompleto" value="<%= request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto") : "" %>"></p>
            <p><label>NIT: </label>
                <input type="text" name="nit" value="<%= request.getParameter("nit") != null ? request.getParameter("nit") : "" %>"></p>
            <p><label>Teléfono: </label>
                <input type="text" name="telefono" value="<%= request.getParameter("telefono") != null ? request.getParameter("telefono") : "" %>"></p>
            <p><label>Dirección: </label>
                <input type="text" name="direccion" value="<%= request.getParameter("direccion") != null ? request.getParameter("direccion") : "" %>"></p>
            <p><label>Correo: </label>
                <input type="text" name="correo" value="<%= request.getParameter("correo") != null ? request.getParameter("correo") : "" %>"></p>
            <p><label>Contraseña: </label>
                <input type="password" name="password"></p>
            <p><label>ID Sucursal: </label>
                <input type="text" name="idSucursalOrigen" value="<%= request.getParameter("idSucursalOrigen") != null ? request.getParameter("idSucursalOrigen") : "" %>"></p>
            <button type="submit">Crear Admin</button>
        </form>

        <a href="panelAdminSistema.jsp">Volver</a>
    </body>
</html>
