<%-- 
    Document   : formularioAdminSucursal
    Created on : 17 sept 2026, 17:53:04
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Sucursal"%>
<%@page import="com.mycompany.transportes.modelo.Usuario"%>
<%
    Usuario a = (Usuario) request.getAttribute("admin");
    boolean edicion = a != null;
    List<Sucursal> sucursales = (List<Sucursal>) request.getAttribute("sucursales");
    String idSucSel = edicion ? String.valueOf(a.getIdSucursalOrigen())
            : (request.getParameter("idSucursalOrigen") != null ? request.getParameter("idSucursalOrigen") : "");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= edicion ? "Editar Admin de Sucursal" : "Nuevo Admin de Sucursal"%></title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1><%= edicion ? "Editar Administrador de Sucursal" : "Nuevo Administrador de Sucursal"%></h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvAdminSistema" method="POST">
                <input type="hidden" name="accion" value="<%= edicion ? "editar" : "crear"%>">

                <p><label>DPI: </label>
                    <input type="text" name="dpi" maxlength="13" <%= edicion ? "readonly" : "required"%>
                           value="<%= edicion ? a.getDpi() : (request.getParameter("dpi") != null ? request.getParameter("dpi") : "")%>"></p>

                <p><label>Nombre completo: </label>
                    <input type="text" name="nombreCompleto" required
                           value="<%= edicion ? a.getNombreCompleto() : (request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto") : "")%>"></p>

                <p><label>NIT (8 dígitos): </label>
                    <input type="text" name="nit" maxlength="8" required
                           value="<%= edicion ? a.getNit() : (request.getParameter("nit") != null ? request.getParameter("nit") : "")%>"></p>

                <p><label>Teléfono (8 dígitos): </label>
                    <input type="text" name="telefono" maxlength="8" required
                           value="<%= edicion ? a.getTelefono() : (request.getParameter("telefono") != null ? request.getParameter("telefono") : "")%>"></p>

                <p><label>Dirección: </label>
                    <input type="text" name="direccion" required
                           value="<%= edicion ? a.getDireccion() : (request.getParameter("direccion") != null ? request.getParameter("direccion") : "")%>"></p>

                <p><label>Correo: </label>
                    <input type="text" name="correo" required
                           value="<%= edicion ? a.getCorreo() : (request.getParameter("correo") != null ? request.getParameter("correo") : "")%>"></p>

                <p><label>Contraseña <%= edicion ? "(déjala vacía para no cambiarla)" : ""%>: </label>
                    <input type="password" name="password" <%= edicion ? "" : "required"%>></p>

                <p><label>Sucursal: </label>
                    <select name="idSucursalOrigen" required>
                        <option value="">-- Seleccione --</option>
                        <% if (sucursales != null) {
                                for (Sucursal s : sucursales) {
                        %>
                        <option value="<%= s.getIdSucursal()%>" <%= String.valueOf(s.getIdSucursal()).equals(idSucSel) ? "selected" : ""%>>
                            <%= s.getNombre()%>
                        </option>
                        <%      }
                            }%>
                    </select></p>

                <button type="submit"><%= edicion ? "Guardar Cambios" : "Crear Admin"%></button>
            </form>
            <p><a href="SvAdminSistema">Volver a la lista</a></p>
        </div>
    </body>
</html>