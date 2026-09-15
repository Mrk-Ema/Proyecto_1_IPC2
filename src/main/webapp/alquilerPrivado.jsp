<%-- 
    Document   : alquilerPrivado
    Created on : 12 sept 2026, 6:33:47
    Author     : mrk-ema
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Solicitar Alquiler Privado - Transportes MRK</title>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <div class="contenido">
            <h2>Solicitar viaje de alquiler privado</h2>
            <p>El precio se calcula por el tiempo del alquiler: <strong>Q150 por hora</strong>
               más <strong>Q50 por pasajero</strong>. El servicio siempre se cotiza como ida y vuelta.</p>

            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>
            <p style="color: red"><%= error%></p>
            <% } %>

            <% if (usuarioActual == null) { %>
            <p>Para solicitar un alquiler debes
                <a href="SvLogin?accion=sinRegistro">iniciar sesión</a> o
                <a href="registro.jsp">crear tu cuenta</a>.</p>
            <% } else { %>
            <form action="SvAlquiler" method="post">
                <input type="hidden" name="accion" value="solicitar">
                <label>Origen:
                    <input type="text" name="origen" required>
                </label><br>
                <label>Destino:
                    <input type="text" name="destino" required>
                </label><br>
                <label>Fecha y hora de salida:
                    <input type="datetime-local" name="fechaSalida" required>
                </label><br>
                <label>Fecha y hora de retorno:
                    <input type="datetime-local" name="fechaRetorno" required>
                </label><br>
                <label>Número de pasajeros:
                    <input type="number" name="numeroPasajeros" min="1" max="45" required>
                </label><br><br>
                <button type="submit">Solicitar y ver precio estimado</button>
            </form>
            <% } %>
        </div>
    </body>
</html>