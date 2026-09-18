<%-- 
    Document   : alquileresPendientes
    Created on : 16 sept 2026, 9:20:32
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alquileres por Confirmar - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            List<AlquilerPrivado> lista = (List<AlquilerPrivado>) request.getAttribute("pendientes");
            String mensaje = (String) request.getAttribute("mensaje");
        %>
        <div class="contenido">
            <h2>Alquileres Privados por Confirmar (Pendientes)</h2>

            <% if (mensaje != null) {%>
            <p style="color: blue; font-weight: bold;"><%= mensaje%></p>
            <% } %>

            <% if (lista == null || lista.isEmpty()) { %>
            <p>No hay alquileres pendientes de confirmación.</p>
            <% } else { %>
            <table border="1" cellpadding="8">
                <tr>
                    <th>N°</th>
                    <th>Cliente (DPI)</th>
                    <th>Origen → Destino</th>
                    <th>Salida</th>
                    <th>Retorno</th>
                    <th>Pasajeros</th>
                    <th>Estimado</th>
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
                    <td>Q<%= String.format("%.2f", a.getPrecioEstimado())%></td>
                    <td>
                        <!-- Formulario de Confirmación -->
                        <form action="SvAlquiler" method="post" style="display:inline">
                            <input type="hidden" name="accion" value="confirmar">
                            <input type="hidden" name="idAlquiler" value="<%= a.getIdAlquiler()%>">
                            <label>Q Confirmado: 
                                <input type="number" step="0.1" name="precioConfirmado" value="<%= a.getPrecioEstimado()%>" required style="width: 80px;">
                            </label>
                            <button type="submit" style="background-color: #28a745; color: white; cursor: pointer;">Confirmar</button>
                        </form>

                        <!-- Formulario de Rechazo -->
                        <form action="SvAlquiler" method="post" style="display:inline; margin-left: 10px;">
                            <input type="hidden" name="accion" value="rechazar">
                            <input type="hidden" name="idAlquiler" value="<%= a.getIdAlquiler()%>">
                            <button type="submit" style="background-color: #dc3545; color: white; cursor: pointer;" onclick="return confirm('¿Estás seguro de rechazar esta solicitud de alquiler?')">Rechazar</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
            <% }%>
            <p><a href="panelAdminSucursal.jsp">Volver al Panel de Control</a></p>
        </div>
    </body>
</html>