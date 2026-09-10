<%-- 
    Document   : login
    Created on : 9 sept 2026, 3:28:45
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Bienvenido a Transportes Alamo </h1>
        <% if (request.getAttribute("error") != null) {%>
        <p style="color:red;"><%= request.getAttribute("error")%></p>

        <% }%>
        <form action="SvLogin" method="POST">
            <h3>Ingresar</h3>
            <p><label>Correo: </label> <input type="text" name="correo" value="<%= request.getParameter("correo") != null ? request.getParameter("correo") : ""%>"></p>  
            <p><label>Contraseña: </label> <input type="password" name="password"> </p>   
            <button type="submit">Ingresar</button>
        </form>
        <p>---------------------- o ----------------------</p>
        <button type="button" onclick="location.href = 'registro.jsp'">Registrarse</button>
        <button type="button" onclick="location.href = 'home.jsp'">Invitado</button>
    </body>
</html>
