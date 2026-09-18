<%-- 
    Document   : misAlquileres
    Created on : 15 sept 2026, 12:44:00
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mis Alquileres - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            List<AlquilerPrivado> lista = (List<AlquilerPrivado>) request.getAttribute("alquileres");
            String error = (String) request.getAttribute("error");
            Double faltante = (Double) request.getAttribute("faltante");
        %>
        <div class="contenido">
            <h2>Mis Alquileres</h2>
            <p><a href="SvAlquiler">Nueva solicitud</a></p>

            <% if (error != null) { %>
            <p style="color: red"><%= error%></p>
            <% } %>
            <% if (faltante != null) { %>
            <p>Recarga tu cartera para completar el pago:
                <a href="recargar.jsp"><button>Recargar cartera</button></a></p>
            <% } %>

            <% if (lista == null || lista.isEmpty()) { %>
            <p>No tienes solicitudes de alquiler.</p>
            <% } else { %>
            <table border="1">
                <tr><th>N°</th><th>Origen → Destino</th><th>Salida</th><th>Retorno</th>
                    <th>Pasajeros</th><th>Precio estimado</th><th>Precio confirmado</th>
                    <th>Estado</th><th>Acción</th></tr>
                <% for (AlquilerPrivado a : lista) {
                    String estado = a.getEstadoPago(); %>
                <tr>
                    <td><%= a.getIdAlquiler()%></td>
                    <td><%= a.getOrigen()%> → <%= a.getDestino()%></td>
                    <td><%= a.getFechaSalida()%></td>
                    <td><%= a.getFechaRetorno()%></td>
                    <td><%= a.getNumeroPasajeros()%></td>
                    <td>Q<%= String.format("%.2f", a.getPrecioEstimado())%></td>
                    <td><%= (a.getPrecioConfirmado() > 0) ? "Q" + String.format("%.2f", a.getPrecioConfirmado()) : "—"%></td>
                    <td><%= estado%></td>
                    <td>
                        <% if ("CONFIRMADO".equals(estado)) { %>
                        <form action="SvAlquiler" method="post" style="display:inline">
                            <input type="hidden" name="accion" value="pagar">
                            <input type="hidden" name="idAlquiler" value="<%= a.getIdAlquiler()%>">
                            <label>Contraseña:
                                <input type="password" name="password" required>
                            </label>
                            <button type="submit">Pagar Q<%= String.format("%.2f", a.getPrecioConfirmado())%></button>
                        </form>
                        <% } else if ("SOLICITADO".equals(estado)) { %>
                        Pendiente de confirmación
                        <% } else if ("PAGADO".equals(estado)) { %>
                        Pagado el <%= a.getFechaPago()%>
                        <% } else { %>
                        Rechazada
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>
            <% } %>
        </div>
    </body>
</html>