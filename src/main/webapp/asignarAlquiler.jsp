<%-- 
    Document   : asignarAlquiler
    Created on : 17 sept 2026, 10:47:16
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.AlquilerPrivado"%>
<%@page import="com.mycompany.transportes.modelo.Bus"%>
<%@page import="com.mycompany.transportes.modelo.Chofer"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Asignar Bus y Chofer - Transportes MRK</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            AlquilerPrivado a = (AlquilerPrivado) request.getAttribute("alquiler");
            List<Bus> buses = (List<Bus>) request.getAttribute("buses");
            List<Chofer> choferes = (List<Chofer>) request.getAttribute("choferes");
        %>
        <div class="contenido">
            <h2>Asignar Bus y Chofer al Alquiler #<%= a != null ? a.getIdAlquiler() : ""%></h2>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% }%>

            <% if (a != null) {%>
            <p><strong>Cliente:</strong> <%= a.getDpiCliente()%></p>
            <p><strong>Origen → Destino:</strong> <%= a.getOrigen()%> → <%= a.getDestino()%></p>
            <p><strong>Salida:</strong> <%= a.getFechaSalida()%> &nbsp;&nbsp; <strong>Retorno:</strong> <%= a.getFechaRetorno()%></p>
            <p><strong>Pasajeros:</strong> <%= a.getNumeroPasajeros()%> &nbsp;&nbsp; <strong>Precio confirmado:</strong> Q<%= String.format("%.2f", a.getPrecioConfirmado())%></p>

            <form action="SvAlquiler" method="POST">
                <input type="hidden" name="accion" value="asignar">
                <input type="hidden" name="idAlquiler" value="<%= a.getIdAlquiler()%>">

                <p><label>Bus (disponibles en la fecha de salida): </label>
                    <select name="idBus" required>
                        <option value="">Seleccione un bus</option>
                        <%
                            if (buses != null) {
                                for (Bus b : buses) {
                        %>
                        <option value="<%= b.getIdBus()%>"><%= b.getPlaca()%> - <%= b.getMarca()%> <%= b.getModelo()%> (capacidad <%= b.getCapacidadPasajeros()%>)</option>
                        <%
                                }
                            }
                        %>
                    </select></p>

                <p><label>Chofer (disponibles en la fecha de salida): </label>
                    <select name="dpiChofer" required>
                        <option value="">Seleccione un chofer</option>
                        <%
                            if (choferes != null) {
                                for (Chofer c : choferes) {
                        %>
                        <option value="<%= c.getDpi()%>"><%= c.getNombreCompleto()%> (<%= c.getDpi()%>)</option>
                        <%
                                }
                            }
                        %>
                    </select></p>

                <p><em>Nota: el salario del chofer para alquiler privado es su salario base +15%.</em></p>

                <button type="submit">Asignar Bus y Chofer</button>
            </form>
            <% }%>

            <p><a href="SvAlquiler?accion=pagados">Volver a Alquileres Pagados</a></p>
        </div>
    </body>
</html>