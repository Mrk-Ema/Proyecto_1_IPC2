<%-- 
    Document   : compraExitosa
    Created on : 14 sept 2026, 11:04:57
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.ViajeDisponible, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Compra Exitosa - Transportes MRK</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            ViajeDisponible v = (ViajeDisponible) request.getAttribute("viaje");
            List<Integer> asientos = (List<Integer>) request.getAttribute("asientos");
            Double total = (Double) request.getAttribute("total");
        %>
        <div class="contenido">
            <h2>¡Compra exitosa!</h2>
            <p><%= v.getNombreOrigen()%> → <%= v.getNombreDestino()%> | <%= v.getFechaHoraSalida()%></p>
            <p>Asientos: <%= asientos%></p>
            <p>Total pagado: Q<%= total%></p>
            <p>Saldo restante: Q<%= String.format("%.2f", usuarioActual.getSaldoCartera())%></p>
            <p><a href="SvViajeRegular">Volver al catálogo</a></p>
        </div>
    </body>
</html>