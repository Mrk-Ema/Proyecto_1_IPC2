<%-- 
    Document   : gestionarChoferes
    Created on : 16 sept 2026, 21:38:26
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Chofer"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Choferes</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Choferes</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p style="color:green;"><%= request.getAttribute("msj")%></p>
            <% } %>

            <a href="crearChofer.jsp">Nuevo Chofer</a> |
            <a href="panelAdminSucursal.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>DPI</th>
                    <th>Nombre</th>
                    <th>Foto</th>
                    <th>Licencia</th>
                    <th>Tipo</th>
                    <th>Vencimiento</th>
                    <th>Salario base</th>
                    <th>Disponibilidad</th>
                    <th>Estado</th>
                    <th>Viajes prog./tránsito</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Chofer> choferes = (List<Chofer>) request.getAttribute("choferes");
                    if (choferes != null) {
                        for (Chofer c : choferes) {
                %>
                <tr>
                    <td><%= c.getDpi()%></td>
                    <td><%= c.getNombreCompleto()%></td>
                    <td>
                        <% if (c.getFoto() != null && !c.getFoto().isEmpty()) {%>
                        <img src="<%= c.getFoto()%>" width="50" height="40">
                        <% }%>
                    </td>
                    <td><%= c.getNumLicencia()%></td>
                    <td><%= c.getTipoLicencia()%></td>
                    <td><%= c.getFechaVencimientoLicencia()%></td>
                    <td>Q<%= String.format("%.2f", c.getSalarioBaseViaje())%></td>
                    <td><%= c.getDisponibilidad()%></td>
                    <td><%= c.isEstado() ? "Activo" : "Inactivo"%></td>
                    <td>
                        <% if (c.getViajesActivos() == 0) { %>
                        <span style="color:green;">Ninguno</span>
                        <% } else {%>
                        <span style="color:red;"><%= c.getViajesActivos()%> en curso</span>
                        <% }%>
                    </td>
                    <td>
                        <a href="SvChofer?accion=formEditar&dpi=<%= c.getDpi()%>">Editar</a>
                        <% if (c.isEstado()) {%>
                        <form action="SvChofer" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="desactivar">
                            <input type="hidden" name="dpi" value="<%= c.getDpi()%>">
                            <button type="submit">Desactivar</button>
                        </form>
                        <% } else {%>
                        <form action="SvChofer" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="activar">
                            <input type="hidden" name="dpi" value="<%= c.getDpi()%>">
                            <button type="submit">Activar</button>
                        </form>
                        <% } %>
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