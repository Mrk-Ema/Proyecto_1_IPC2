<%-- 
    Document   : gestionarViajes
    Created on : 17 sept 2026, 8:36:32
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Viaje"%>
<%!
    public String mostrarEstado(String estado) {
        switch (estado == null ? "" : estado) {
            case "PROGRAMADO":
                return "Programado";
            case "EN_TRANSITO":
                return "En tránsito";
            case "FINALIZADO":
                return "Finalizado";
            case "CANCELADO":
                return "Cancelado";
            default:
                return estado == null ? "" : estado;
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Viajes</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Viajes</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p style="color:green;"><%= request.getAttribute("msj")%></p>
            <% } %>

            <a href="SvViaje?accion=formCrear">Nuevo Viaje</a> |
            <a href="panelAdminSucursal.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>ID</th>
                    <th>Tipo</th>
                    <th>Ruta</th>
                    <th>Bus</th>
                    <th>Chofer</th>
                    <th>Salida estimada</th>
                    <th>Llegada estimada</th>
                    <th>Estado</th>
                    <th>Salida real</th>
                    <th>KM inicial</th>
                    <th>Llegada real</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Viaje> viajes = (List<Viaje>) request.getAttribute("viajes");
                    if (viajes != null) {
                        for (Viaje v : viajes) {
                %>
                <tr>
                    <td><%= v.getIdViaje()%></td>
                    <td><%= "REGULAR".equals(v.getTipoViaje()) ? "Regular" : "Alquiler"%></td>
                    <td><%= v.getNombreRuta() != null ? v.getNombreRuta() : "—"%></td>
                    <td><%= v.getPlacaBus() != null ? v.getPlacaBus() : "—"%></td>
                    <td><%= v.getNombreChofer() != null ? v.getNombreChofer() : "—"%></td>
                    <td><%= v.getFechaHoraSalidaEstimada()%></td>
                    <td><%= v.getFechaHoraLlegadaEstimada()%></td>
                    <td><%= mostrarEstado(v.getEstadoOperativo())%></td>
                    <td><%= v.getFechaHoraSalidaReal() != null ? v.getFechaHoraSalidaReal() : "—"%></td>
                    <td><%= v.getKilometrajeInicial() > 0 || v.getFechaHoraSalidaReal() != null ? v.getKilometrajeInicial() : "—"%></td>
                    <td><%= v.getFechaHoraLlegadaReal() != null ? v.getFechaHoraLlegadaReal() : "—"%></td>
                    <td>
                        <% if ("PROGRAMADO".equals(v.getEstadoOperativo())) {%>
                        <a href="SvViaje?accion=formEditar&idViaje=<%= v.getIdViaje()%>">Editar</a>
                        <a href="SvViaje?accion=formSalida&idViaje=<%= v.getIdViaje()%>">Registrar Salida</a>
                        <% } %>
                        <% if ("EN_TRANSITO".equals(v.getEstadoOperativo())) {%>
                        <a href="SvViaje?accion=formLlegada&idViaje=<%= v.getIdViaje()%>">Registrar Llegada</a>
                        <% } %>
                        <% if ("PROGRAMADO".equals(v.getEstadoOperativo()) && "REGULAR".equals(v.getTipoViaje())) {%>
                        <form action="SvViaje" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="idViaje" value="<%= v.getIdViaje()%>">
                            <button type="submit" onclick="return confirm('¿Eliminar este viaje?');">Eliminar</button>
                        </form>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>