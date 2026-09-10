<%-- 
    Document   : registrar
    Created on : 9 sept 2026, 3:38:14
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello Estas en el apartado de registro</h1>

        <% if (request.getAttribute("error") != null) {%>
        <p style="color:red;"><%= request.getAttribute("error")%></p>

        <% }%>

        <form action="SvUsuario" method="POST">
            <input type="hidden" name="tipoRegistro" value="cliente">
            <p><label>DPI: </label>
                <input type="text" name="dpi" value="<%= request.getParameter("dpi") != null ? request.getParameter("dpi") : ""%>"></p>
            <p><label>Nombre completo: </label>
                <input type="text" name="nombreCompleto" value="<%= request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto") : ""%>"></p>
            <p><label>NIT: </label>
                <input type="text" name="nit" value="<%= request.getParameter("nit") != null ? request.getParameter("nit") : ""%>"></p>
            <p><label>Teléfono: </label>
                <input type="text" name="telefono" value="<%= request.getParameter("telefono") != null ? request.getParameter("telefono") : ""%>"></p>
            <p><label>Dirección: </label>
                <input type="text" name="direccion" value="<%= request.getParameter("direccion") != null ? request.getParameter("direccion") : ""%>"></p>
            <p><label>Correo: </label>
                <input type="text" name="correo" value="<%= request.getParameter("correo") != null ? request.getParameter("correo") : ""%>"></p>
            <p><label>Contraseña: </label>
                <input type="password" name="password"></p>
            <button type="submit">Registrarse</button>
        </form>

        <p>¿Ya tienes cuenta? <button type="button" onclick="location.href = 'login.jsp'">Inicia Sesion</button></p>
    </body>
</html>
