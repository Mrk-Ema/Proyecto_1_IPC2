/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transportes.servicio;

import com.mycompany.transportes.dao.ViajeDAO;
import com.mycompany.transportes.modelo.ViajeDisponible;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author mrk-ema
 */
public class ViajeRegularService {

    private final ViajeDAO viajeDAO = new ViajeDAO();

    // Obtener catálogo bruto
    public List<ViajeDisponible> obtenerCatalogoCompleto() throws SQLException {
        return viajeDAO.obtenerDisponiblesDetallado();
    }

    public ViajeDisponible obtenerViajeDisponiblePorId(int idViaje) throws SQLException {
        return viajeDAO.obtenerDisponibleDetalladoPorId(idViaje);
    }

    // Filtrar catálogo por origen, destino y rango de fecha
    public List<ViajeDisponible> filtrarViajesDeCatalogo(List<ViajeDisponible> lista,
            String origen, String destino, String fechaInicio, String fechaFin) {
        List<ViajeDisponible> filtroNuevo = new ArrayList<>();

        // Normalizar para no fallar con vacíos o null
        origen = (origen == null || origen.trim().isEmpty()) ? null : origen.trim();
        destino = (destino == null || destino.trim().isEmpty()) ? null : destino.trim();
        fechaInicio = (fechaInicio == null || fechaInicio.trim().isEmpty()) ? null : fechaInicio.trim();
        fechaFin = (fechaFin == null || fechaFin.trim().isEmpty()) ? null : fechaFin.trim();

        for (ViajeDisponible v : lista) {

            if (origen != null && !v.getNombreOrigen().equalsIgnoreCase(origen)) {
                continue;
            }
            if (destino != null && !v.getNombreDestino().equalsIgnoreCase(destino)) {
                continue;
            }
            if (fechaInicio != null && fechaFin != null) {
                String f = v.getFechaSalidaFormateada();
                if (f.compareTo(fechaInicio) < 0 || f.compareTo(fechaFin) > 0) {
                    continue;
                }
            }
            filtroNuevo.add(v);
        }
        return filtroNuevo;
    }

    // Orígenes únicos que tienen viajes programados
    public Set<String> obtenerOrigenesDisponibles(List<ViajeDisponible> catalogoBruto) {
        Set<String> origenes = new TreeSet<>();
        for (ViajeDisponible v : catalogoBruto) {
            origenes.add(v.getNombreOrigen());
        }
        return origenes;
    }

    // Destinos unicos desde un origen elegido
    public Set<String> obtenerDestinosDisponiblesParaOrigen(List<ViajeDisponible> catalogoBruto, String origen) {
        Set<String> destinos = new TreeSet<>();
        for (ViajeDisponible v : catalogoBruto) {
            if (origen == null || origen.isEmpty() || v.getNombreOrigen().equalsIgnoreCase(origen)) {
                destinos.add(v.getNombreDestino());
            }
        }
        return destinos;
    }

    public boolean hayFiltro(String origen, String destino, String fechaInicio, String fechaFin) {
        return (origen != null && !origen.trim().isEmpty())
                || (destino != null && !destino.trim().isEmpty())
                || (fechaInicio != null && !fechaInicio.trim().isEmpty())
                || (fechaFin != null && !fechaFin.trim().isEmpty());

    }

}
