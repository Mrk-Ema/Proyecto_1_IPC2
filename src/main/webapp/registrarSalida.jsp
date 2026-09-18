<%-- 
    Document   : registrarSalida
    Created on : 17 sept 2026, 12:51:03
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Viaje"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrar Salida - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            Viaje v = (Viaje) request.getAttribute("viaje");
            String kmActual = request.getAttribute("kmActual") != null
                    ? String.valueOf(request.getAttribute("kmActual")) : "";
            String fechaActual = (String) request.getAttribute("fechaActual");
        %>
        <div class="contenido">
            <h2>Registrar Salida - Viaje #<%= v != null ? v.getIdViaje() : ""%></h2>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% }%>

            <% if (v != null) {%>
            <p><strong>Estado:</strong> <%= v.getEstadoOperativo()%></p>
            <p><strong>Ruta:</strong> <%= v.getNombreRuta() != null ? v.getNombreRuta() : "—"%></p>
            <p><strong>Bus:</strong> <%= v.getPlacaBus() != null ? v.getPlacaBus() : "—"%> &nbsp;&nbsp;
                <strong>Chofer:</strong> <%= v.getNombreChofer() != null ? v.getNombreChofer() : "—"%></p>
            <p><strong>Salida estimada:</strong> <%= v.getFechaHoraSalidaEstimada()%>
                &nbsp;&nbsp; <strong>Llegada estimada:</strong> <%= v.getFechaHoraLlegadaEstimada()%></p>

            <form action="SvViaje" method="POST">
                <input type="hidden" name="accion" value="salida">
                <input type="hidden" name="idViaje" value="<%= v.getIdViaje()%>">

                <p><label>Fecha y hora real de salida: </label>
                    <input type="datetime-local" name="fechaHoraSalidaReal" value="<%= fechaActual%>" required></p>

                <p><label>Kilometraje inicial (km): </label>
                    <input type="number" step="0.01" min="0" name="kmInicial" value="<%= kmActual%>" required></p>

                <button type="submit" class="btn btn-primary">Registrar Salida</button>
            </form>
            <% }%>

            <p><a href="SvViaje" class="btn btn-link">Volver a la lista</a></p>
        </div>
    </body>
</html>