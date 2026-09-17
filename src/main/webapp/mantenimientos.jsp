<%-- 
    Document   : mantenimientos
    Created on : 17 sept 2026, 14:53:01
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Mantenimiento"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Historial de Mantenimientos</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Historial de Gastos de Taller</h1>

            <% if (request.getAttribute("msj") != null) {%>
            <p style="color:green;"><%= request.getAttribute("msj")%></p>
            <% } %>
            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>

            <a href="SvMantenimiento?accion=formCrear">Nuevo Registro</a> |
            <a href="panelAdminSucursal.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>ID</th>
                    <th>Placa</th>
                    <th>Mano de obra</th>
                    <th>Repuestos</th>
                    <th>Total</th>
                    <th>Fecha</th>
                    <th>Descripción</th>
                </tr>
                <%
                    List<Mantenimiento> lista = (List<Mantenimiento>) request.getAttribute("mantenimientos");
                    if (lista != null) {
                        for (Mantenimiento m : lista) {
                %>
                <tr>
                    <td><%= m.getIdMantenimiento()%></td>
                    <td><%= m.getPlaca()%></td>
                    <td><%= String.format("Q%.2f", m.getMontoManoObra())%></td>
                    <td><%= String.format("Q%.2f", m.getMontoRepuestos())%></td>
                    <td><%= String.format("Q%.2f", m.getMontoManoObra() + m.getMontoRepuestos())%></td>
                    <td><%= m.getFechaMantenimiento()%></td>
                    <td><%= m.getDescripcion() != null ? m.getDescripcion() : "—"%></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>