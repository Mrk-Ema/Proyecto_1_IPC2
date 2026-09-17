<%-- 
    Document   : panelAdminSucursal
    Created on : 10 sept 2026, 3:17:04
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
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Panel de Administrador de sucursal</h1>
            <a href="SvChofer">Gestionar Choferes</a> |
            <a href="SvBus">Gestionar Buses</a> |
            <a href="SvRuta">Gestionar Rutas</a> |
            <a href="SvViaje">Gestionar Viajes</a> |
            <a href="SvAlquiler?accion=pendientes">Alquileres por Confirmar</a> |
            <a href="SvAlquiler?accion=pagados">Alquileres Pagados</a> |
            <a href="SvMantenimiento">Gastos de Taller</a> 
        </div>
    </body>
</html>
