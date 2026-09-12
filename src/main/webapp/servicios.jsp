<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Servicios - Transportes MRK</title>
    </head>
    <body>
  
        <%@ include file="header.jsp" %>

        <div class="contenido">
            <h1>Servicios</h1>
            <p>Ofrecemos dos tipos de servicio para adaptarnos a tus necesidades de viaje.</p>

            <h2>Viajes Regulares</h2>
            <p>Rutas programadas diariamente entre nuestras sucursales. Selecciona tu origen y destino, elige fecha y hora, y reserva tu boleto.</p>
            <ul>
                <li>Horarios fijos diarios</li>
                <li>Precios accesibles por ruta</li>
                <li>Asientos numerados</li>
            </ul>
            <a href="#">Ver rutas disponibles</a>

            <h2>Alquiler Privado</h2>
            <p>Necesitas un bus exclusivo para tu grupo? Alquila un bus privado con chofer dedicado para tu empresa, escuela o evento.</p>
            <ul>
                <li>Servicio exclusivo para tu grupo</li>
                <li>Chofer asignado</li>
                <li>Recogida en tu ubicación</li>
            </ul>
           

            <% if (usuarioActual == null) { %>
            <p><em>Para reservar o solicitar un servicio, debes 
                    <a href="login.jsp">iniciar sesión</a> o 
                    <a href="registro.jsp">crear una cuenta</a>.</em></p>
                    <% } else { %>
            <a href="#">Reservar boleto</a>
            <a href="#">Solicitar alquiler</a>
            <% }%>

        </div>
    </body>
</html>
