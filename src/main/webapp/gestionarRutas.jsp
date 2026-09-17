<%-- 
    Document   : gestionarRutas
    Created on : 17 sept 2026, 7:08:04
    Author     : mrk-ema
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Ruta"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Rutas</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Rutas</h1>

            <% if (request.getAttribute("error") != null) { %>
                <p style="color:red;"><%= request.getAttribute("error") %></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) { %>
                <p style="color:green;"><%= request.getAttribute("msj") %></p>
            <% } %>

            <a href="SvRuta?accion=formCrear">Nueva Ruta</a> |
            <a href="panelAdminSucursal.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>ID</th>
                    <th>Origen</th>
                    <th>Destino</th>
                    <th>Distancia</th>
                    <th>Precio boleto</th>
                    <th>Estado</th>
                    <th>Viajes asociados</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Ruta> rutas = (List<Ruta>) request.getAttribute("rutas");
                    if (rutas != null) {
                        for (Ruta r : rutas) {
                %>
                <tr>
                    <td><%= r.getIdRuta() %></td>
                    <td><%= r.getNombreOrigen() %></td>
                    <td><%= r.getNombreDestino() %></td>
                    <td><%= String.format("%.2f", r.getDistanciaKm()) %> km</td>
                    <td>Q<%= String.format("%.2f", r.getPrecioBoleto()) %></td>
                    <td><%= r.isEstado() ? "Activa" : "Inactiva" %></td>
                    <td>
                        <% if (r.getViajesActivos() == 0) { %>
                            <span style="color:green;">Ninguno</span>
                        <% } else { %>
                            <span style="color:red;"><%= r.getViajesActivos() %> asociado(s)</span>
                        <% } %>
                    </td>
                    <td>
                        <form action="SvRuta" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="idRuta" value="<%= r.getIdRuta() %>">
                            <button type="submit">Eliminar</button>
                        </form>
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