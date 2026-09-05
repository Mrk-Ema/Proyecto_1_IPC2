package com.mycompany.transportes.modelo;

public class Viaje {

    private int idViaje;
    private int idBus;
    private String dpiChofer;
    private int idRuta;
    private String tipoViaje;
    private String fechaHoraSalidaEstimada;
    private String fechaHoraLlegadaEstimada;
    private String fechaHoraSalidaReal;
    private double kilometrajeInicial;
    private String fechaHoraLlegadaReal;
    private double kilometrajeFinal;
    private double gastoCombustible;
    private double montoDepreciacion;
    private String estadoOperativo;

    public Viaje() {
    }

    public Viaje(int idRuta, String tipoViaje, String fechaHoraSalidaEstimada, String fechaHoraLlegadaEstimada) {
        this.idRuta = idRuta;
        this.tipoViaje = tipoViaje;
        this.fechaHoraSalidaEstimada = fechaHoraSalidaEstimada;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getDpiChofer() {
        return dpiChofer;
    }

    public void setDpiChofer(String dpiChofer) {
        this.dpiChofer = dpiChofer;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getTipoViaje() {
        return tipoViaje;
    }

    public void setTipoViaje(String tipoViaje) {
        this.tipoViaje = tipoViaje;
    }

    public String getFechaHoraSalidaEstimada() {
        return fechaHoraSalidaEstimada;
    }

    public void setFechaHoraSalidaEstimada(String fechaHoraSalidaEstimada) {
        this.fechaHoraSalidaEstimada = fechaHoraSalidaEstimada;
    }

    public String getFechaHoraLlegadaEstimada() {
        return fechaHoraLlegadaEstimada;
    }

    public void setFechaHoraLlegadaEstimada(String fechaHoraLlegadaEstimada) {
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public String getFechaHoraSalidaReal() {
        return fechaHoraSalidaReal;
    }

    public void setFechaHoraSalidaReal(String fechaHoraSalidaReal) {
        this.fechaHoraSalidaReal = fechaHoraSalidaReal;
    }

    public double getKilometrajeInicial() {
        return kilometrajeInicial;
    }

    public void setKilometrajeInicial(double kilometrajeInicial) {
        this.kilometrajeInicial = kilometrajeInicial;
    }

    public String getFechaHoraLlegadaReal() {
        return fechaHoraLlegadaReal;
    }

    public void setFechaHoraLlegadaReal(String fechaHoraLlegadaReal) {
        this.fechaHoraLlegadaReal = fechaHoraLlegadaReal;
    }

    public double getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(double kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }

    public double getGastoCombustible() {
        return gastoCombustible;
    }

    public void setGastoCombustible(double gastoCombustible) {
        this.gastoCombustible = gastoCombustible;
    }

    public double getMontoDepreciacion() {
        return montoDepreciacion;
    }

    public void setMontoDepreciacion(double montoDepreciacion) {
        this.montoDepreciacion = montoDepreciacion;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }
}
