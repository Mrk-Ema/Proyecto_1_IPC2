<%-- 
    Document   : alquileresPagados
    Created on : 17 sept 2026, 10:46:49
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alquileres Pagados - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            List<AlquilerPrivado> lista = (List<AlquilerPrivado>) request.getAttribute("pagados");
            String mensaje = (String) request.getAttribute("mensaje");
        %>
        <div class="contenido">
            <h2>Alquileres Pagados - Asignar Bus y Chofer</h2>

            <% if (mensaje != null) {%>
            <p style="color: blue; font-weight: bold;"><%= mensaje%></p>
            <% } %>

            <% if (lista == null || lista.isEmpty()) { %>
            <p>No hay alquileres pagados pendientes de asignación.</p>
            <% } else { %>
            <table border="1" cellpadding="8">
                <tr>
                    <th>N°</th>
                    <th>Cliente (DPI)</th>
                    <th>Origen → Destino</th>
                    <th>Salida</th>
                    <th>Retorno</th>
                    <th>Pasajeros</th>
                    <th>Precio Confirmado</th>
                    <th>Acciones</th>
                </tr>
                <% for (AlquilerPrivado a : lista) {%>
                <tr>
                    <td><%= a.getIdAlquiler()%></td>
                    <td><%= a.getDpiCliente()%></td>
                    <td><strong><%= a.getOrigen()%> → <%= a.getDestino()%></strong></td>
                    <td><%= a.getFechaSalida()%></td>
                    <td><%= a.getFechaRetorno()%></td>
                    <td><%= a.getNumeroPasajeros()%></td>
                    <td>Q<%= String.format("%.2f", a.getPrecioConfirmado())%></td>
                    <td>
                        <a href="SvAlquiler?accion=formAsignar&idAlquiler=<%= a.getIdAlquiler()%>">
                            <button type="button" style="background-color: #17a2b8; color: white; cursor: pointer;">Asignar Bus y Chofer</button>
                        </a>
                    </td>
                </tr>
                <% } %>
            </table>
            <% }%>
            <p><a href="panelAdminSucursal.jsp">Volver al Panel de Control</a></p>
        </div>
    </body>
</html>