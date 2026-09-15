<%-- 
    Document   : viajesRegulares
    Created on : 12 sept 2026, 6:33:26
    Author     : mrk-ema
--%>

<%@page import="com.mycompany.transportes.modelo.ViajeDisponible"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Viajes Regulares - Transportes MRK</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>

           <% String error = (String) request.getAttribute("error");
           if ("no_disponible".equals(error)) { %>
           <p>El viaje ya no está disponible.</p><% }
             else if ("sistema".equals(error)) { %>
             <p>Error del sistema, intenta de nuevo.</p>
           <% } %>
        
        <div class="contenido">
            <h2>Viajes regulares disponibles</h2>
            <%
                List<String> origenes = (List<String>) request.getAttribute("origenes");
                List<String> destinos = (List<String>) request.getAttribute("destinos");
                List<ViajeDisponible> resultado = (List<ViajeDisponible>) request.getAttribute("resultado");
                String oriSel = (String) request.getAttribute("origenSel");
                String desSel = (String) request.getAttribute("destinoSel");
                String fiSel = (String) request.getAttribute("fechaInicioSel");
                String ffSel = (String) request.getAttribute("fechaFinSel");
            %>

            <form method="get" action="SvViajeRegular">
                <select name="origen">
                    <option value="">Todos los orígenes</option>
                    <% for (String o : origenes) {%>
                    <option value="<%= o%>" <%= o.equals(oriSel) ? "selected" : ""%>><%= o%></option>
                    <% } %>
                </select>
                <select name="destino">
                    <option value="">Todos los destinos</option>
                    <% for (String d : destinos) {%>
                    <option value="<%= d%>" <%= d.equals(desSel) ? "selected" : ""%>><%= d%></option>
                    <% }%>
                </select>
                Desde: <input type="date" name="fechaInicio" value="<%= fiSel != null ? fiSel : ""%>">
                Hasta: <input type="date" name="fechaFin"   value="<%= ffSel != null ? ffSel : ""%>">
                <button type="submit">Filtrar</button>
            </form>

            <% if (resultado == null || resultado.isEmpty()) { %>
            <p>No hay viajes que coincidan con tu búsqueda.</p>
            <% } else { %>
            <table border="1">
                <tr><th>Origen</th><th>Destino</th><th>Salida</th><th>Llegada</th><th>Precio</th><th>Asientos libres</th><th></th></tr>
                        <% for (ViajeDisponible v : resultado) {%>
                <tr>
                    <td><%= v.getNombreOrigen()%></td>
                    <td><%= v.getNombreDestino()%></td>
                    <td><%= v.getFechaHoraSalida()%></td>
                    <td><%= v.getFechaHoraLlegada()%></td>
                    <td>Q<%= v.getPrecioBoleto()%></td>
                    <td><%= v.getAsientosLibresCount()%></td>
                    <td><a href="SvViajeRegular?accion=asientos&idViaje=<%= v.getIdViaje()%>">Elegir Asientos</a></td>
                </tr>
                <% } %>
            </table>
            <% }%>
        </div>
    </body>
</html>
