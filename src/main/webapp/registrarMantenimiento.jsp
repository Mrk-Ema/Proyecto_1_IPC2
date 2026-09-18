<%-- 
    Document   : registrarMantenimiento
    Created on : 17 sept 2026, 14:52:28
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.transportes.modelo.Bus"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrar Mantenimiento</title>
            <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <%
            List<Bus> buses = (List<Bus>) request.getAttribute("buses");
            String idBusSel = request.getParameter("idBus") != null ? request.getParameter("idBus") : "";
            String manoObra = request.getParameter("montoManoObra") != null ? request.getParameter("montoManoObra") : "";
            String repuestos = request.getParameter("montoRepuestos") != null ? request.getParameter("montoRepuestos") : "";
            String fechaSel = request.getParameter("fechaMantenimiento") != null ? request.getParameter("fechaMantenimiento") : "";
            String descSel = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
        %>
        <div class="contenido">
            <h1>Registrar Mantenimiento / Gastos de Taller</h1>

            <% if (request.getAttribute("error") != null) {%>
            <p class="alert alert-danger"><%= request.getAttribute("error")%></p>
            <% }%>

            <form action="SvMantenimiento" method="POST">
                <input type="hidden" name="accion" value="crear">

                <p><label>Bus: </label>
                    <select name="idBus" required>
                        <option value="">-- Seleccione --</option>
                        <% if (buses != null) {
                                for (Bus b : buses) {
                                    if (!b.isEstado()) {
                                        continue;
                                    }
                        %>
                        <option value="<%= b.getIdBus()%>" <%= String.valueOf(b.getIdBus()).equals(idBusSel) ? "selected" : ""%>>
                            <%= b.getPlaca()%> — <%= b.getMarca()%> <%= b.getModelo()%>
                        </option>
                        <%      }
                            }%>
                    </select></p>

                <p><label>Monto mano de obra (Q): </label>
                    <input type="number" step="0.01" min="0" name="montoManoObra" value="<%= manoObra%>" required></p>

                <p><label>Monto repuestos (Q): </label>
                    <input type="number" step="0.01" min="0" name="montoRepuestos" value="<%= repuestos%>" required></p>

                <p><label>Fecha del mantenimiento: </label>
                    <input type="date" name="fechaMantenimiento" value="<%= fechaSel%>" required></p>

                <p><label>Descripción (opcional): </label>
                    <textarea name="descripcion" rows="2" cols="40"><%= descSel%></textarea></p>

                <button type="submit" class="btn btn-primary">Registrar</button>
            </form>
            <p><a href="SvMantenimiento" class="btn btn-link">Ver historial</a> <a href="panelAdminSucursal.jsp" class="btn btn-link">Volver al panel</a></p>
        </div>
    </body>
</html>