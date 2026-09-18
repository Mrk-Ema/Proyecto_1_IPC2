<%-- 
    Document   : viajesRegulares
    Created on : 12 sept 2026, 6:33:26
    Author     : mrk-ema
--%>

<%@page import="com.mycompany.transportes.modelo.ViajeDisponible"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Viajes Regulares - Transportes MRK</title>
        <jsp:include page="/includes/resources.jsp"/>
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
                        <c:forEach items="${resultado}" var="v">
                <tr>
                    <td>${v.nombreOrigen}</td>
                    <td>${v.nombreDestino}</td>
                    <td>${v.fechaHoraSalida}</td>
                    <td>${v.fechaHoraLlegada}</td>
                    <td>Q${v.precioBoleto}</td>
                    <td>${v.asientosLibresCount}</td>
                    <td><a href="SvViajeRegular?accion=asientos&idViaje=${v.idViaje}">Elegir Asientos</a></td>
                </tr>
                </c:forEach>
            </table>
            <% }%>
        </div>
    </body>
</html>
