<%-- 
    Document   : pagoAlquilerExito
    Created on : 16 sept 2026, 10:01:13
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pago Realizado - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            AlquilerPrivado a = (AlquilerPrivado) request.getAttribute("alquiler");
            if (a != null) {
        %>
        <div class="contenido">
            <h2>Pago realizado con éxito</h2>
            <p><strong><%= a.getOrigen()%> → <%= a.getDestino()%></strong></p>
            <p>Salida: <%= a.getFechaSalida()%></p>
            <p>Retorno: <%= a.getFechaRetorno()%></p>
            <p>Pasajeros: <%= a.getNumeroPasajeros()%></p>
            <p>Total pagado: <strong>Q<%= String.format("%.2f", a.getPrecioConfirmado())%></strong></p>
            <p>Estado: <strong>PAGADO</strong> (<%= a.getFechaPago()%>)</p>
            <p>Tu nuevo saldo aparece en el menú superior.</p>
            <p><a href="SvAlquiler?accion=mis">Ver Mis Alquileres</a> | <a href="home.jsp">Volver al inicio</a></p>
        </div>
        <% } else { %>
        <p>No se encontró la solicitud. <a href="SvAlquiler?accion=mis">Ir a Mis Alquileres</a>.</p>
        <% }%>
    </body>
</html>