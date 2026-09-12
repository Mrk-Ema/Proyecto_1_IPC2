<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear Chofer</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <h1>Crear Chofer</h1>

        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>

        <form action="SvUsuario" method="POST">
            <input type="hidden" name="tipoRegistro" value="chofer">
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
            <p><label>Número de licencia: </label>
                <input type="text" name="numLicencia" value="<%= request.getParameter("numLicencia") != null ? request.getParameter("numLicencia") : "" %>"></p>
            <p><label>Tipo de licencia: </label>
                <select name="tipoLicencia">
                    <option value="A">A</option>
                    <option value="B">B</option>
                </select></p>
            <p><label>Fecha vencimiento licencia: </label>
                <input type="date" name="fechaVencimientoLicencia"></p>
            <p><label>Salario base por viaje: </label>
                <input type="text" name="salarioBaseViaje" value="<%= request.getParameter("salarioBaseViaje") != null ? request.getParameter("salarioBaseViaje") : "" %>"></p>
            <p><label>Foto (URL): </label>
                <input type="text" name="foto" value="<%= request.getParameter("foto") != null ? request.getParameter("foto") : "" %>"></p>
            <button type="submit">Crear Chofer</button>
        </form>

        <a href="panelAdminSucursal.jsp">Volver</a>
    </body>
</html>
