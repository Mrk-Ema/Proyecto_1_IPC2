package com.mycompany.transportes.modelo;

import java.io.Serializable;
import java.util.List;

public class ViajeDisponible implements Serializable {

    private int idViaje;
    private int idRuta;
    private String nombreOrigen;
    private String nombreDestino;
    private String fechaHoraSalida;
    private String fechaHoraLlegada;
    private double precioBoleto;
    private String placaBus;
    private int capacidadPasajeros;
    private List<Integer> asientosOcupados;

    public ViajeDisponible() {
    }

    // Getters y Setters
    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombreOrigen() {
        return nombreOrigen;
    }

    public void setNombreOrigen(String nombreOrigen) {
        this.nombreOrigen = nombreOrigen;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(String fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }

    public String getPlacaBus() {
        return placaBus;
    }

    public void setPlacaBus(String placaBus) {
        this.placaBus = placaBus;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public List<Integer> getAsientosOcupados() {
        return asientosOcupados;
    }

    public void setAsientosOcupados(List<Integer> asientosOcupados) {
        this.asientosOcupados = asientosOcupados;
    }

    public int getAsientosLibresCount() {
        return capacidadPasajeros - (asientosOcupados != null ? asientosOcupados.size() : 0);
    }

    public String getFechaSalidaFormateada() {
        if (fechaHoraSalida != null && fechaHoraSalida.length() >= 10) {
            return fechaHoraSalida.substring(0, 10);
        }
        return fechaHoraSalida;
    }

}
