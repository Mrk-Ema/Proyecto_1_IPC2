<%-- 
    Document   : gestionarAdminSucursal
    Created on : 17 sept 2026, 17:52:30
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.mycompany.transportes.modelo.Usuario"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionar Administradores de Sucursal</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Administradores de Sucursal</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p class="alert alert-success"><%= request.getAttribute("msj")%></p>
            <% } %>

            <a href="SvAdminSistema?accion=formCrear" class="btn btn-primary">Nuevo Admin de Sucursal</a>
            <a href="panelAdminSistema.jsp" class="btn btn-link">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>DPI</th>
                    <th>Nombre</th>
                    <th>Sucursal</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Usuario> admins = (List<Usuario>) request.getAttribute("admins");
                    Map<Integer, String> mapa = (Map<Integer, String>) request.getAttribute("mapaSucursal");
                    if (admins != null) {
                        for (Usuario a : admins) {
                %>
                <tr>
                    <td><%= a.getDpi()%></td>
                    <td><%= a.getNombreCompleto()%></td>
                    <td><%= a.getIdSucursalOrigen() > 0 && mapa != null ? (mapa.get(a.getIdSucursalOrigen()) != null ? mapa.get(a.getIdSucursalOrigen()) : "—") : "—"%></td>
                    <td><%= a.getCorreo()%></td>
                    <td><%= a.isEstado() ? "Activo" : "Inactivo"%></td>
                    <td>
                        <a href="SvAdminSistema?accion=formEditar&dpi=<%= a.getDpi()%>" class="btn btn-sm btn-outline-warning">Editar</a>
                        <% if (a.isEstado()) {%>
                        <form action="SvAdminSistema" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="desactivar">
                            <input type="hidden" name="dpi" value="<%= a.getDpi()%>">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Desactivar</button>
                        </form>
                        <% } else {%>
                        <form action="SvAdminSistema" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="activar">
                            <input type="hidden" name="dpi" value="<%= a.getDpi()%>">
                            <button type="submit" class="btn btn-sm btn-outline-success">Activar</button>
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