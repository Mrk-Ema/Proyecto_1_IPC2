<%-- 
    Document   : seleccionAsientos
    Created on : 13 sept 2026, 4:47:40
    Author     : mrk-ema
--%>

<%@page import="com.mycompany.transportes.modelo.ViajeDisponible"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Selección de Asientos - Transportes MRK</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <div class="contenido">
            <h2>Selecciona tus asientos</h2>

            <%
                String error = request.getParameter("error");
                String recarga = request.getParameter("recarga");
                ViajeDisponible v = (ViajeDisponible) request.getAttribute("viajeDisponible");
                boolean esCliente = usuarioActual != null && "CLIENTE".equals(usuarioActual.getRol());
            %>

            <% if ("asientos_tomados".equals(error)) { %>
            <p style="color: red">Un asiento que elegiste ya no está disponible. Elige de nuevo.</p>
            <% } %>
            <% if (recarga != null) { %>
            <p style="color: green">Saldo recargado. Puedes continuar.</p>
            <% } %>

            <% if (v == null) { %>
            <p>Viaje no disponible. <a href="SvViajeRegular">Ver catálogo</a></p>
            <% } else {
                int libres = v.getAsientosLibresCount();
                boolean lleno = libres <= 0;
            %>
            <p><strong><%= v.getNombreOrigen()%>  →  <%= v.getNombreDestino()%></strong><br>
                Salida:  <%= v.getFechaHoraSalida()%><br>
                Llegada:  <%= v.getFechaHoraLlegada()%><br>
                Bus: <%= v.getPlacaBus()%>  |  Precio boleto: Q<%= v.getPrecioBoleto()%><br>
                Asientos disponibles: <%= libres%></p>

            <% if (esCliente) {%>
            <p style="color: green">Tu saldo: <strong>Q<%= String.format("%.2f", usuarioActual.getSaldoCartera())%></strong></p>
            <% } %>
            <% if (!esCliente) { %>
            <p>Para seleccionar asientos debes
                <a href="SvLogin?accion=sinRegistro">iniciar sesión</a> o
                <a href="registro.jsp">crear tu cuenta</a>.</p>
                <% } else if (lleno) { %>
            <p>Este viaje ya no tiene asientos disponibles.</p>
            <% } else {%>
            <form action="SvBoleto" method="post">
                <input type="hidden" name="accion" value="preparar">
                <input type="hidden" name="idViaje" value="<%= v.getIdViaje()%>">

                <table>
                    <c:set var="esCliente" value="${sessionScope.usuario != null && sessionScope.usuario.rol == 'CLIENTE'}"/>
                    <tr>
                        <c:forEach begin="1" end="${viajeDisponible.capacidadPasajeros}" var="i">
                            <c:set var="tomado" value="${viajeDisponible.asientosOcupados.contains(i)}"/>
                        <td>
                            <label>
                                <input type="checkbox" name="asientos" value="${i}"
                                       ${(tomado || !esCliente) ? "disabled" : ""}>
                                ${i}${tomado ? " (ocupado)" : ""}
                            </label>
                        </td>
                        <c:if test="${i % 6 == 0}">
                            </tr><tr>
                        </c:if>
                        </c:forEach>
                    </tr>
                </table>

                <button type="submit" <%= (!esCliente || lleno) ? "disabled" : ""%>>
                    Continuar al pago
                </button>
            </form>
            <% } %>
            <% }%>
        </div>
    </body>
</html>