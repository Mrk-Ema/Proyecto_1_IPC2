<%-- 
    Document   : registrarLlegada
    Created on : 17 sept 2026, 13:24:08
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Viaje"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrar Llegada - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            Viaje v = (Viaje) request.getAttribute("viaje");
            String kmBus = request.getAttribute("kmBus") != null
                    ? String.valueOf(request.getAttribute("kmBus")) : "";
            String fechaActual = (String) request.getAttribute("fechaActual");
        %>
        <div class="contenido">
            <h2>Registrar Llegada - Viaje #<%= v != null ? v.getIdViaje() : ""%></h2>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% }%>

            <% if (v != null) {%>
            <p><strong>Estado:</strong> <%= v.getEstadoOperativo()%></p>
            <p><strong>Ruta:</strong> <%= v.getNombreRuta() != null ? v.getNombreRuta() : "—"%></p>
            <p><strong>Bus:</strong> <%= v.getPlacaBus() != null ? v.getPlacaBus() : "—"%> &nbsp;&nbsp;
                <strong>Chofer:</strong> <%= v.getNombreChofer() != null ? v.getNombreChofer() : "—"%></p>
            <p><strong>Salida real:</strong> <%= v.getFechaHoraSalidaReal()%>
                &nbsp;&nbsp; <strong>KM inicial:</strong> <%= v.getKilometrajeInicial()%></p>

            <form action="SvViaje" method="POST">
                <input type="hidden" name="accion" value="llegada">
                <input type="hidden" name="idViaje" value="<%= v.getIdViaje()%>">

                <p><label>Fecha y hora real de llegada: </label>
                    <input type="datetime-local" name="fechaHoraLlegadaReal" value="<%= fechaActual%>" required></p>

                <p><label>Kilometraje final (km): </label>
                    <input type="number" step="0.01" min="0" name="kmFinal" value="<%= kmBus%>" required></p>

                <p><label>Gasto de combustible (Q): </label>
                    <input type="number" step="0.01" min="0" name="gastoCombustible" value="0.00" required></p>

                <p><em>La depreciación se calcula automáticamente según la configuración del sistema.</em></p>

                <button type="submit" class="btn btn-primary">Registrar Llegada</button>
            </form>
            <% }%>

            <p><a href="SvViaje" class="btn btn-link">Volver a la lista</a></p>
        </div>
    </body>
</html>