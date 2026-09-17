package com.mycompany.transportes.modelo;

public class Ruta {

    private int idRuta;
    private int idSucursalOrigen;
    private int idSucursalDestino;
    private double distanciaKm;
    private double precioBoleto;
    private boolean estado;
    private String nombreOrigen;
    private String nombreDestino;
    private int viajesActivos;

    public Ruta() {
    }

    public Ruta(int idSucursalOrigen, int idSucursalDestino, double distanciaKm, double precioBoleto) {
        this.idSucursalOrigen = idSucursalOrigen;
        this.idSucursalDestino = idSucursalDestino;
        this.distanciaKm = distanciaKm;
        this.precioBoleto = precioBoleto;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public void setIdSucursalOrigen(int idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public int getIdSucursalDestino() {
        return idSucursalDestino;
    }

    public void setIdSucursalDestino(int idSucursalDestino) {
        this.idSucursalDestino = idSucursalDestino;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public int getViajesActivos() {
        return viajesActivos;
    }

    public void setViajesActivos(int viajesActivos) {
        this.viajesActivos = viajesActivos;
    }
}
