<%-- 
    Document   : recargar
    Created on : 14 sept 2026, 8:48:01
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recargar Cartera - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            if (usuarioActual == null) {
                response.sendRedirect("SvLogin?accion=sinRegistro");
                return;
            }
        %>
        <div class="contenido">
            <h2>Recargar cartera</h2>
            <p>Saldo actual: <strong>Q<%= String.format("%.2f", usuarioActual.getSaldoCartera())%></strong></p>

            <form action="SvUsuario" method="post">
                <input type="hidden" name="tipoRegistro" value="recargar">
                <input type="hidden" name="idViaje" value="<%= request.getParameter("idViaje")%>">
                <label>Monto a recargar (Q):
                    <input type="number" step="0.1" min="1" name="monto" required>
                </label>
                <button type="submit">Recargar</button>
            </form>

            <% if (request.getParameter("idViaje") != null) {%>
            <p><a href="SvViajeRegular?accion=asientos&idViaje=<%= request.getParameter("idViaje")%>">Volver al viaje</a></p>
            <% } else { %>
            <p><a href="SvViajeRegular">Volver al catálogo</a></p>
            <% }%>
        </div>
    </body>
</html>