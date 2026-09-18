<%-- 
    Document   : alquilerSolicitudExito
    Created on : 14 sept 2026, 12:46:30
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Solicitud Registrada - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            AlquilerPrivado a = (AlquilerPrivado) request.getAttribute("alquiler");
        %>
        <div class="contenido">
            <h2>Solicitud registrada</h2>
            <p><strong><%= a.getOrigen()%> → <%= a.getDestino()%></strong></p>
            <p>Salida: <%= a.getFechaSalida()%></p>
            <p>Retorno: <%= a.getFechaRetorno()%></p>
            <p>Pasajeros: <%= a.getNumeroPasajeros()%></p>
            <p>Precio estimado: <strong>Q<%= String.format("%.2f", a.getPrecioEstimado())%></strong></p>
            <p>Estado: SOLICITADO</p>
            <p>Un administrador de sucursal confirmará el precio. Cuando lo confirme podrás pagar desde tu cartera.</p>
            <p><a href="home.jsp">Volver al inicio</a></p>
        </div>
    </body>
</html>