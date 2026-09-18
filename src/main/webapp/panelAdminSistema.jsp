<%-- 
    Document   : panelAdminSistema
    Created on : 10 sept 2026, 3:06:47
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Panel de Administrador del Sistema</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <div class="contenido">
            <h1>Panel de Administrador del Sistema</h1>
            <p><a href="SvSucursal" class="btn btn-primary">Gestionar Sucursales</a></p>
            <p><a href="SvAdminSistema" class="btn btn-primary">Gestionar Admins de Sucursal</a></p>
            <p><a href="SvAdminSistema?accion=usuarios" class="btn btn-primary">Gestionar Usuarios</a></p>
            <p><a href="SvAdminSistema?accion=formConfig" class="btn btn-primary">Configuración</a></p>
        </div>

    </body>
</html>
