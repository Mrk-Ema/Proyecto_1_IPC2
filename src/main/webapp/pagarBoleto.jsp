<%-- 
    Document   : pagarBoleto
    Created on : 14 sept 2026, 8:41:19
    Author     : mrk-ema
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.ViajeDisponible, java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmar Pago - Transportes MRK</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            ViajeDisponible v = (ViajeDisponible) request.getAttribute("viaje");
            List<Integer> asientos = (List<Integer>) request.getAttribute("asientos");
            Double total = (Double) request.getAttribute("total");
            Double saldo = (Double) request.getAttribute("saldo");
            Double faltante = (Double) request.getAttribute("faltante");
            String error = (String) request.getAttribute("error");
        %>
        <div class="contenido">
            <h2>Confirmación de pago</h2>
            <p><strong>${viaje.nombreOrigen} → ${viaje.nombreDestino}</strong></p>
            <p>Salida: ${viaje.fechaHoraSalida} | Bus: ${viaje.placaBus}</p>
            <p>Asientos: ${asientos}</p>
            <p>Total a pagar: <strong>Q${total}</strong></p>
            <p>Tu saldo: Q${saldo}</p>

            <% if (error != null) { %>
            <p style="color: red"><%= error%></p>
            <% } %>

            <form action="SvBoleto" method="post">
                <input type="hidden" name="accion" value="pagar">
                <input type="hidden" name="idViaje" value="${viaje.idViaje}">
                <c:forEach items="${asientos}" var="a">
                <input type="hidden" name="asientos" value="${a}">
                </c:forEach>
                <label>Contraseña para autorizar:
                    <input type="password" name="password" required>
                </label>
                <button type="submit">Pagar Q${total}</button>
            </form>

            <% if (faltante != null) { %>
            <p>Te faltan Q<%= faltante%>. Recarga tu cartera para continuar.</p>
            <a href="recargar.jsp?idViaje=<%= v.getIdViaje()%>&asientos=<%= asientos.toString().replace("[", "").replace("]", "").replace(" ", "")%>">
                <button>Recargar cartera</button>
            </a>
            <% } %>

            <p><a href="SvViajeRegular">Volver al catálogo</a></p>
        </div>
    </body>
</html>