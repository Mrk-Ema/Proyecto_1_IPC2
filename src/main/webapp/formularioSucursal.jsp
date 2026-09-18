<%-- 
    Document   : formularioSucursal
    Created on : 17 sept 2026, 17:04:50
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Sucursal"%>
<%
    Sucursal s = (Sucursal) request.getAttribute("sucursal");
    boolean edicion = s != null;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= edicion ? "Editar Sucursal" : "Nueva Sucursal"%></title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1><%= edicion ? "Editar Sucursal" : "Nueva Sucursal"%></h1>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvSucursal" method="POST">
                <input type="hidden" name="accion" value="<%= edicion ? "editar" : "crear"%>">
                <% if (edicion) {%>
                <input type="hidden" name="idSucursal" value="<%= s.getIdSucursal()%>">
                <% }%>

                <p><label>Nombre: </label>
                    <input type="text" name="nombre"
                           value="<%= edicion ? s.getNombre() : (request.getParameter("nombre") != null ? request.getParameter("nombre") : "")%>" required></p>

                <p><label>Dirección: </label>
                    <input type="text" name="direccion"
                           value="<%= edicion ? s.getDireccion() : (request.getParameter("direccion") != null ? request.getParameter("direccion") : "")%>" required></p>

                <p><label>Teléfono: </label>
                    <input type="text" name="telefono" maxlength="15"
                           value="<%= edicion ? (s.getTelefono() != null ? s.getTelefono() : "") : (request.getParameter("telefono") != null ? request.getParameter("telefono") : "")%>"></p>

                <button type="submit" class="btn btn-primary"><%= edicion ? "Guardar Cambios" : "Crear Sucursal"%></button>
            </form>
            <p><a href="SvSucursal" class="btn btn-link">Volver a la lista</a></p>
        </div>
    </body>
</html>