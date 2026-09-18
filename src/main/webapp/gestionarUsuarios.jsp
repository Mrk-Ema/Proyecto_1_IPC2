<%-- 
    Document   : gestionarUsuarios
    Created on : 17 sept 2026, 18:22:42
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
        <title>Gestionar Usuarios</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Gestionar Usuarios</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getAttribute("msj") != null) {%>
            <p style="color:green;"><%= request.getAttribute("msj")%></p>
            <% } %>

            <a href="panelAdminSistema.jsp">Volver al panel</a>

            <table border="1" cellpadding="5">
                <tr>
                    <th>DPI</th>
                    <th>Nombre</th>
                    <th>Rol</th>
                    <th>Sucursal</th>
                    <th>Correo</th>
                    <th>Saldo</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                <%
                    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                    Map<Integer, String> mapa = (Map<Integer, String>) request.getAttribute("mapaSucursal");
                    if (usuarios != null) {
                        for (Usuario u : usuarios) {
                %>
                <tr>
                    <td><%= u.getDpi()%></td>
                    <td><%= u.getNombreCompleto()%></td>
                    <td><%= u.getRol()%></td>
                    <td><%= u.getIdSucursalOrigen() > 0 && mapa != null && mapa.get(u.getIdSucursalOrigen()) != null ? mapa.get(u.getIdSucursalOrigen()) : "—"%></td>
                    <td><%= u.getCorreo()%></td>
                    <td><%= String.format("Q%.2f", u.getSaldoCartera())%></td>
                    <td><%= u.isEstado() ? "Activo" : "Inactivo"%></td>
                    <td>
                        <% if (u.isEstado()) {%>
                        <form action="SvAdminSistema" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="desactivarUsuario">
                            <input type="hidden" name="dpi" value="<%= u.getDpi()%>">
                            <button type="submit">Desactivar</button>
                        </form>
                        <% } else {%>
                        <form action="SvAdminSistema" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="activarUsuario">
                            <input type="hidden" name="dpi" value="<%= u.getDpi()%>">
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