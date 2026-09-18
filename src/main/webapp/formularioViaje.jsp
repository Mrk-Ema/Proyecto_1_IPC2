<%-- 
    Document   : formularioViaje
    Created on : 17 sept 2026, 8:37:02
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Viaje"%>
<%@page import="com.mycompany.transportes.modelo.Bus"%>
<%@page import="com.mycompany.transportes.modelo.Chofer"%>
<%@page import="com.mycompany.transportes.modelo.Ruta"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario Viaje</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            Viaje viaje = (Viaje) request.getAttribute("viaje");
            boolean esEdicion = viaje != null;
            String salidaVal = esEdicion && viaje.getFechaHoraSalidaEstimada() != null
                    ? viaje.getFechaHoraSalidaEstimada().replace(" ", "T") : "";
            String llegadaVal = esEdicion && viaje.getFechaHoraLlegadaEstimada() != null
                    ? viaje.getFechaHoraLlegadaEstimada().replace(" ", "T") : "";
            List<Bus> buses = (List<Bus>) request.getAttribute("buses");
            List<Chofer> choferes = (List<Chofer>) request.getAttribute("choferes");
            List<Ruta> rutas = (List<Ruta>) request.getAttribute("rutas");
            int idBusSel = esEdicion ? viaje.getIdBus() : 0;
            String dpiChoferSel = esEdicion ? (viaje.getDpiChofer() != null ? viaje.getDpiChofer() : "") : "";
            int idRutaSel = esEdicion ? viaje.getIdRuta() : 0;
        %>
        <div class="contenido">
            <h1><%= esEdicion ? "Editar Viaje" : "Nuevo Viaje"%></h1>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvViaje" method="POST">
                <input type="hidden" name="accion" value="<%= esEdicion ? "editar" : "crear"%>">
                <% if (esEdicion) {%>
                <input type="hidden" name="idViaje" value="<%= viaje.getIdViaje()%>">
                <% }%>

                <p><label>Tipo de viaje: </label>
                    <input type="text" readonly value="Viaje Regular"></p>

                <p><label>Fecha y hora de salida estimada: </label>
                    <input type="datetime-local" name="fechaHoraSalidaEstimada" value="<%= salidaVal%>" required></p>
                <p><label>Fecha y hora de llegada estimada: </label>
                    <input type="datetime-local" name="fechaHoraLlegadaEstimada" value="<%= llegadaVal%>" required></p>

                <p><label>Bus: </label>
                    <select name="idBus" required>
                        <option value="">Seleccione un bus</option>
                        <%
                            if (buses != null) {
                                for (Bus b : buses) {
                                    String sel = b.getIdBus() == idBusSel ? "selected" : "";
                        %>
                        <option value="<%= b.getIdBus()%>" <%= sel%>><%= b.getPlaca()%> - <%= b.getMarca()%> <%= b.getModelo()%> (<%= b.getDisponibilidad()%>)</option>
                        <%
                                }
                            }
                        %>
                    </select></p>

                <p><label>Chofer: </label>
                    <select name="dpiChofer" required>
                        <option value="">Seleccione un chofer</option>
                        <%
                            if (choferes != null) {
                                for (Chofer c : choferes) {
                                    String sel = c.getDpi().equals(dpiChoferSel) ? "selected" : "";
                        %>
                        <option value="<%= c.getDpi()%>" <%= sel%>><%= c.getNombreCompleto()%> (<%= c.getDpi()%>)</option>
                        <%
                                }
                            }
                        %>
                    </select></p>

                <p><label>Ruta: </label>
                    <select name="idRuta">
                        <option value="">Seleccione una ruta</option>
                        <%
                            if (rutas != null) {
                                for (Ruta r : rutas) {
                                    String sel = r.getIdRuta() == idRutaSel ? "selected" : "";
                        %>
                        <option value="<%= r.getIdRuta()%>" <%= sel%>><%= r.getNombreOrigen()%> → <%= r.getNombreDestino()%></option>
                        <%
                                }
                            }
                        %>
                    </select></p>

                <button type="submit" class="btn btn-primary"><%= esEdicion ? "Guardar Cambios" : "Crear Viaje"%></button>
            </form>

            <a href="SvViaje" class="btn btn-link">Volver a la lista</a>
        </div>
    </body>
</html>