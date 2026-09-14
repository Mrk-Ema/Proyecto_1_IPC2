/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.transportes.servlets;

import com.mycompany.transportes.modelo.ViajeDisponible;
import com.mycompany.transportes.servicio.ViajeRegularService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mrk-ema
 */
@WebServlet(name = "SvViajeRegular", urlPatterns = {"/SvViajeRegular"})
public class SvViajeRegular extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("asientos".equals(accion)) {
            int idViaje;
            try {
                idViaje = Integer.parseInt(request.getParameter("idViaje"));
            } catch (NumberFormatException e) {
                response.sendRedirect("SvViajeRegular?accion=catalogo&error=id_invalido");
                return;
            }

            ViajeDisponible viaje;
            try {
                viaje = new ViajeRegularService().obtenerViajeDisponiblePorId(idViaje);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("SvViajeRegular?accion=catalogo&error=sistema");
                return;
            }

            if (viaje == null) {        // null = ya no existe o no está PROGRAMADO o  talves ruta o bus inactivo
                response.sendRedirect("SvViajeRegular?accion=catalogo&error=no_disponible");
                return;
            }

            request.setAttribute("viajeDisponible", viaje);
            request.getRequestDispatcher("seleccionAsientos.jsp").forward(request, response);
            return;
        }

        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        try {
            ViajeRegularService service = new ViajeRegularService();
            List<ViajeDisponible> catalogo = service.obtenerCatalogoCompleto();

            List<ViajeDisponible> resultado = service.hayFiltro(origen, destino, fechaInicio, fechaFin)
                    ? service.filtrarViajesDeCatalogo(catalogo, origen, destino, fechaInicio, fechaFin)
                    : catalogo;

            request.setAttribute("origenes", new ArrayList<>(service.obtenerOrigenesDisponibles(catalogo)));
            request.setAttribute("destinos", new ArrayList<>(service.obtenerDestinosDisponiblesParaOrigen(catalogo, origen)));
            request.setAttribute("resultado", resultado);
            request.setAttribute("origenSel", origen);
            request.setAttribute("destinoSel", destino);
            request.setAttribute("fechaInicioSel", fechaInicio);
            request.setAttribute("fechaFinSel", fechaFin);
            request.setAttribute("error", request.getParameter("error"));
            request.getRequestDispatcher("viajesRegulares.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "sistema");
            request.getRequestDispatcher("viajesRegulares.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
