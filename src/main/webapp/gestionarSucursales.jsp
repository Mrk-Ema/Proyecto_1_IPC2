<%-- 
    Document   : gestionarSucursales
    Created on : 17 sept 2026, 16:45:48
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Sucursal"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Sucursales</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Sucursales</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p style="color:green;"><%= request.getAttribute("msj")%></p>
            <% } %>

            <a href="SvSucursal?accion=formCrear">Nueva Sucursal</a> |
            <a href="panelAdminSistema.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Dirección</th>
                    <th>Teléfono</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Sucursal> lista = (List<Sucursal>) request.getAttribute("sucursales");
                    if (lista != null) {
                        for (Sucursal s : lista) {
                %>
                <tr>
                    <td><%= s.getIdSucursal()%></td>
                    <td><%= s.getNombre()%></td>
                    <td><%= s.getDireccion()%></td>
                    <td><%= s.getTelefono() != null ? s.getTelefono() : "—"%></td>
                    <td><%= s.isEstado() ? "Activa" : "Inactiva"%></td>
                    <td>
                        <a href="SvSucursal?accion=formEditar&idSucursal=<%= s.getIdSucursal()%>">Editar</a>
                        <% if (s.isEstado()) {%>
                        <form action="SvSucursal" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="desactivar">
                            <input type="hidden" name="idSucursal" value="<%= s.getIdSucursal()%>">
                            <button type="submit">Desactivar</button>
                        </form>
                        <% } else {%>
                        <form action="SvSucursal" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="activar">
                            <input type="hidden" name="idSucursal" value="<%= s.getIdSucursal()%>">
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