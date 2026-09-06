package com.mycompany.transportes.modelo;

public class Bus {

    private int idBus;
    private String placa;
    private String foto;
    private String marca;
    private String modelo;
    private int anoFabricacion;
    private int capacidadPasajeros;
    private double kilometrajeActual;
    private int idSucursalOrigen;
    private int idSucursalActual;
    private String disponibilidad;
    private boolean estado;

    public Bus() {
    }

    public Bus(String placa, String foto, String marca, String modelo, int anoFabricacion, int capacidadPasajeros, double kilometrajeActual, int idSucursalOrigen) {
        this.placa = placa;
        this.foto = foto;
        this.marca = marca;
        this.modelo = modelo;
        this.anoFabricacion = anoFabricacion;
        this.capacidadPasajeros = capacidadPasajeros;
        this.kilometrajeActual = kilometrajeActual;
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnoFabricacion() {
        return anoFabricacion;
    }

    public void setAnoFabricacion(int anoFabricacion) {
        this.anoFabricacion = anoFabricacion;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public double getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(double kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }

    public int getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public void setIdSucursalOrigen(int idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public int getIdSucursalActual() {
        return idSucursalActual;
    }

    public void setIdSucursalActual(int idSucursalActual) {
        this.idSucursalActual = idSucursalActual;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
