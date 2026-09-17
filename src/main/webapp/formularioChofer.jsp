<%-- 
    Document   : formularioChofer
    Created on : 16 sept 2026, 21:39:01
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.transportes.modelo.Chofer"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Chofer</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            Chofer c = (Chofer) request.getAttribute("chofer");
        %>
        <div class="contenido">
            <h1>Editar Chofer</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvChofer" method="POST">
                <input type="hidden" name="accion" value="editar">

                <p><label>DPI (no editable): </label>
                    <input type="text" readonly
                           value="<%= c != null ? c.getDpi() : request.getParameter("dpi")%>">
                    <input type="hidden" name="dpi"
                           value="<%= c != null ? c.getDpi() : request.getParameter("dpi")%>"></p>
                <p><label>Nombre: </label>
                    <input type="text" readonly
                           value="<%= c != null ? c.getNombreCompleto() : ""%>"></p>
                <p><label>Teléfono: </label>
                    <input type="text" name="telefono" maxlength="15"
                           value="<%= c != null ? c.getTelefono() : (request.getParameter("telefono") != null ? request.getParameter("telefono") : "")%>"></p>
                <p><label>Dirección: </label>
                    <input type="text" name="direccion"
                           value="<%= c != null ? c.getDireccion() : (request.getParameter("direccion") != null ? request.getParameter("direccion") : "")%>"></p>
                <p><label>Foto (URL): </label>
                    <input type="text" name="foto"
                           value="<%= c != null ? c.getFoto() : (request.getParameter("foto") != null ? request.getParameter("foto") : "")%>"></p>
                <p><label>Número de licencia: </label>
                    <input type="text" name="numLicencia"
                           value="<%= c != null ? c.getNumLicencia() : (request.getParameter("numLicencia") != null ? request.getParameter("numLicencia") : "")%>"></p>
                <p><label>Tipo de licencia: </label>
                    <select name="tipoLicencia">
                        <option value="A" <%= c != null && "A".equals(c.getTipoLicencia()) ? "selected" : ""%>>A</option>
                        <option value="B" <%= c != null && "B".equals(c.getTipoLicencia()) ? "selected" : ""%>>B</option>
                    </select></p>
                <p><label>Fecha vencimiento licencia: </label>
                    <input type="date" name="fechaVencimientoLicencia"
                           value="<%= c != null ? c.getFechaVencimientoLicencia() : (request.getParameter("fechaVencimientoLicencia") != null ? request.getParameter("fechaVencimientoLicencia") : "")%>"></p>
                <p><label>Salario base por viaje: </label>
                    <input type="number" step="0.01" name="salarioBaseViaje"
                           value="<%= c != null ? c.getSalarioBaseViaje() : (request.getParameter("salarioBaseViaje") != null ? request.getParameter("salarioBaseViaje") : "")%>"></p>
                <button type="submit">Guardar Cambios</button>
            </form>

            <a href="SvChofer">Volver a la lista</a>
        </div>
    </body>
</html>