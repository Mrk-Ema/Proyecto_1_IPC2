<%-- 
    Document   : compraExitosa
    Created on : 14 sept 2026, 11:04:57
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Compra Exitosa - Transportes MRK</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h2>¡Compra exitosa!</h2>
            <p>${viaje.nombreOrigen} → ${viaje.nombreDestino} | ${viaje.fechaHoraSalida}</p>
            <p>Asientos: ${asientos}</p>
            <p>Total pagado: Q${total}</p>
            <p>Saldo restante: Q<%= String.format("%.2f", usuarioActual.getSaldoCartera())%></p>
            <p><a href="SvViajeRegular">Volver al catálogo</a></p>
        </div>
    </body>
</html>