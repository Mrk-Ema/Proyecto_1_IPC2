<%-- 
    Document   : configuracionSistema
    Created on : 17 sept 2026, 18:17:12
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String fmt(Object v) {
        return v == null ? "" : String.format("%.2f", Double.parseDouble(v.toString()));
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configuración del Sistema</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contenido">
            <h1>Configuración del Sistema</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
            <% } %>
            <% if (request.getParameter("guardado") != null) {%>
            <p style="color:green;">Monto de depreciación actualizado.</p>
            <% }%>

            <form action="SvAdminSistema" method="POST">
                <input type="hidden" name="accion" value="guardarConfig">
                <p><label>Monto de depreciación por kilómetro recorrido (Q): </label>
                    <input type="number" step="0.01" min="0.01" name="monto"
                           value="<%= fmt(request.getAttribute("monto"))%>" required></p>
                <button type="submit">Guardar</button>
            </form>
            <p><a href="panelAdminSistema.jsp">Volver al panel</a></p>
        </div>
    </body>
</html>