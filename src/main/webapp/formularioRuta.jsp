<%-- 
    Document   : formularioRuta
    Created on : 17 sept 2026, 7:14:01
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Sucursal"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nueva Ruta</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Nueva Ruta</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvRuta" method="POST">
                <input type="hidden" name="accion" value="crear">

                <p><label>Origen: </label>
                    <input type="text" readonly
                           value="<%= request.getAttribute("nombreOrigen") != null ? request.getAttribute("nombreOrigen") : ""%>"></p>
                <p><label>Destino: </label>
                    <select name="idSucursalDestino">
                        <%
                            List<Sucursal> sucursales = (List<Sucursal>) request.getAttribute("sucursales");
                            Integer idOrigen = (Integer) request.getAttribute("idSucursalOrigen");
                            if (sucursales != null) {
                                for (Sucursal s : sucursales) {
                                    if (idOrigen != null && s.getIdSucursal() == idOrigen) {
                                        continue;
                                    }
                                    String sel = request.getParameter("idSucursalDestino") != null
                                            && request.getParameter("idSucursalDestino").equals(String.valueOf(s.getIdSucursal())) ? "selected" : "";
                        %>
                        <option value="<%= s.getIdSucursal()%>" <%= sel%>><%= s.getNombre()%></option>
                        <%
                                }
                            }
                        %>
                    </select></p>
                <p><label>Distancia (km): </label>
                    <input type="number" step="0.01" min="0.01" name="distanciaKm"
                           value="<%= request.getParameter("distanciaKm") != null ? request.getParameter("distanciaKm") : ""%>"></p>
                <p><label>Precio del boleto (Q): </label>
                    <input type="number" step="0.01" min="0.01" name="precioBoleto"
                           value="<%= request.getParameter("precioBoleto") != null ? request.getParameter("precioBoleto") : ""%>"></p>
                <button type="submit">Crear Ruta</button>
            </form>

            <a href="SvRuta">Volver a la lista</a>
        </div>
    </body>
</html>