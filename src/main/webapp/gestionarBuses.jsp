<%-- 
    Document   : gestionarBuses
    Created on : 16 sept 2026, 20:49:42
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Bus"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Buses</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Buses</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p class="alert alert-success"><%= request.getAttribute("msj")%></p>
            <% } %>

            <form action="SvBus" method="GET" style="margin-bottom:10px;">
                <input type="hidden" name="accion" value="listar">
                <label>Estado operativo:</label>
                <select name="filtro">
                    <option value="TODOS" ${empty filtro || filtro == 'TODOS' ? 'selected' : ''}>Todos</option>
                    <option value="Disponible" ${filtro == 'Disponible' ? 'selected' : ''}>Disponible</option>
                    <option value="Ocupado" ${filtro == 'Ocupado' ? 'selected' : ''}>Ocupado</option>
                    <option value="Taller" ${filtro == 'Taller' ? 'selected' : ''}>En taller</option>
                </select>
                <button type="submit" class="btn btn-primary">Filtrar</button>
            </form>
            <a href="SvBus?accion=formCrear" class="btn btn-primary">Nuevo Bus</a>
            <a href="panelAdminSucursal.jsp" class="btn btn-link">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>ID</th>
                    <th>Placa</th>
                    <th>Foto</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Año</th>
                    <th>Capacidad</th>
                    <th>Kilometraje</th>
                    <th>Disponibilidad</th>
                    <th>Viajes prog./tránsito</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Bus> buses = (List<Bus>) request.getAttribute("buses");
                    if (buses != null) {
                        for (Bus b : buses) {
                %>
                <tr>
                    <td><%= b.getIdBus()%></td>
                    <td><%= b.getPlaca()%></td>
                    <td>
                        <% if (b.getFoto() != null && !b.getFoto().isEmpty()) {%>
                        <img src="<%= b.getFoto()%>" width="60" height="40">
                        <% }%>
                    </td>
                    <td><%= b.getMarca()%></td>
                    <td><%= b.getModelo()%></td>
                    <td><%= b.getAñoFabricacion()%></td>
                    <td><%= b.getCapacidadPasajeros()%></td>
                    <td><%= String.format("%.2f", b.getKilometrajeActual())%></td>
                    <td><%= b.getDisponibilidad()%></td>
                    <td>
                        <% if (b.getViajesActivos() == 0) { %>
                        <span style="color:green;">Ninguno</span>
                        <% } else {%>
                        <span style="color:red;"><%= b.getViajesActivos()%> en curso</span>
                        <% }%>
                    </td>
                    <td><%= b.isEstado() ? "Activo" : "Inactivo"%></td>
                    <td>
                        <a href="SvBus?accion=formEditar&idBus=<%= b.getIdBus()%>" class="btn btn-sm btn-outline-warning">Editar</a>
                        <% if (b.isEstado()) {%>
                        <form action="SvBus" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="desactivar">
                            <input type="hidden" name="idBus" value="<%= b.getIdBus()%>">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Desactivar</button>
                        </form>
                        <% } else {%>
                        <form action="SvBus" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="activar">
                            <input type="hidden" name="idBus" value="<%= b.getIdBus()%>">
                            <button type="submit" class="btn btn-sm btn-outline-success">Activar</button>
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