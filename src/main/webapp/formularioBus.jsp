<%-- 
    Document   : formularioBus
    Created on : 16 sept 2026, 20:51:03
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Bus"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario Bus</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            Bus bus = (Bus) request.getAttribute("bus");
            boolean esEdicion = bus != null
                    || (request.getParameter("idBus") != null && !request.getParameter("idBus").isEmpty());
        %>
        <div class="contenido">
            <h1><%= esEdicion ? "Editar Bus" : "Nuevo Bus"%></h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvBus" method="POST">
                <input type="hidden" name="accion" value="<%= esEdicion ? "editar" : "crear"%>">
                <% if (esEdicion) {%>
                <input type="hidden" name="idBus" value="<%= bus != null ? bus.getIdBus() : request.getParameter("idBus")%>">
                <% }%>

                <p><label>Placa: </label>
                    <input type="text" name="placa" value="<%= bus != null ? bus.getPlaca() : (request.getParameter("placa") != null ? request.getParameter("placa") : "")%>"></p>
                <p><label>Foto (URL): </label>
                    <input type="text" name="foto" value="<%= bus != null ? bus.getFoto() : (request.getParameter("foto") != null ? request.getParameter("foto") : "")%>"></p>
                <p><label>Marca: </label>
                    <input type="text" name="marca" value="<%= bus != null ? bus.getMarca() : (request.getParameter("marca") != null ? request.getParameter("marca") : "")%>"></p>
                <p><label>Modelo: </label>
                    <input type="text" name="modelo" value="<%= bus != null ? bus.getModelo() : (request.getParameter("modelo") != null ? request.getParameter("modelo") : "")%>"></p>
                <p><label>Año de fabricación: </label>
                    <input type="number" name="añoFabricacion" value="<%= bus != null ? bus.getAñoFabricacion() : (request.getParameter("añoFabricacion") != null ? request.getParameter("añoFabricacion") : "")%>"></p>
                <p><label>Capacidad de pasajeros: </label>
                    <input type="number" name="capacidadPasajeros" value="<%= bus != null ? bus.getCapacidadPasajeros() : (request.getParameter("capacidadPasajeros") != null ? request.getParameter("capacidadPasajeros") : "")%>"></p>
                <p><label>Kilometraje:

                        <% if (esEdicion) {%>
                        (solo lectura - se actualiza con los viajes)
                    </label>
                    <input type="number" step="0.1" readonly value="<%= String.format("%.2f", bus.getKilometrajeActual())%>"></p>
                    <% } else { %>
                (valor inicial, puede ser 0)
                </label>
                <input type="number" step="0.01" name="kilometrajeActual" value="0"></p>
                <% }%>

                <button type="submit"><%= esEdicion ? "Guardar Cambios" : "Crear Bus"%></button>
            </form>

            <a href="SvBus">Volver a la lista</a>
        </div>
    </body>
</html>