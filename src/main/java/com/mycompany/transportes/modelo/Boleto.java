package com.mycompany.transportes.modelo;

public class Boleto {

    private int idBoleto;
    private int idViaje;
    private String dpiCliente;
    private int numAsiento;
    private double precioPagado;
    private String fechaPago;

    public Boleto() {
    }

    public Boleto(int idViaje, String dpiCliente, int numAsiento, double precioPagado) {
        this.idViaje = idViaje;
        this.dpiCliente = dpiCliente;
        this.numAsiento = numAsiento;
        this.precioPagado = precioPagado;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }

    public int getNumAsiento() {
        return numAsiento;
    }

    public void setNumAsiento(int numAsiento) {
        this.numAsiento = numAsiento;
    }

    public double getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(double precioPagado) {
        this.precioPagado = precioPagado;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
