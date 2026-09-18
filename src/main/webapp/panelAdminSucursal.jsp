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
        <title>Panel de Administrador de Sucursal</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Panel de Administrador de sucursal</h1>
            <p><a href="SvChofer" class="btn btn-primary">Gestionar Choferes</a></p>
            <p><a href="SvBus" class="btn btn-primary">Gestionar Buses</a></p>
            <p><a href="SvRuta" class="btn btn-primary">Gestionar Rutas</a></p>
            <p><a href="SvViaje" class="btn btn-primary">Gestionar Viajes</a></p>
            <p><a href="SvAlquiler?accion=pendientes" class="btn btn-primary">Alquileres por Confirmar</a></p>
            <p><a href="SvAlquiler?accion=pagados" class="btn btn-primary">Alquileres Pagados</a></p>
            <p><a href="SvMantenimiento" class="btn btn-primary">Gastos de Taller</a></p>
        </div>
    </body>
</html>
