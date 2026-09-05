package com.mycompany.transportes.modelo;

public class AlquilerPrivado {

    private int idAlquiler;
    private String dpiCliente;
    private int idViaje;
    private String origen;
    private String destino;
    private String fechaSalida;
    private String fechaRetorno;
    private int numeroPasajeros;
    private double precioEstimado;
    private double precioConfirmado;
    private double salarioChoferCalculado;
    private String estadoPago;
    private String fechaPago;

    public AlquilerPrivado() {
    }

    public AlquilerPrivado(String dpiCliente, String origen, String destino, String fechaSalida, String fechaRetorno, int numeroPasajeros, double precioEstimado) {
        this.dpiCliente = dpiCliente;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaRetorno = fechaRetorno;
        this.numeroPasajeros = numeroPasajeros;
        this.precioEstimado = precioEstimado;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getFechaRetorno() {
        return fechaRetorno;
    }

    public void setFechaRetorno(String fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public int getNumeroPasajeros() {
        return numeroPasajeros;
    }

    public void setNumeroPasajeros(int numeroPasajeros) {
        this.numeroPasajeros = numeroPasajeros;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    public double getPrecioConfirmado() {
        return precioConfirmado;
    }

    public void setPrecioConfirmado(double precioConfirmado) {
        this.precioConfirmado = precioConfirmado;
    }

    public double getSalarioChoferCalculado() {
        return salarioChoferCalculado;
    }

    public void setSalarioChoferCalculado(double salarioChoferCalculado) {
        this.salarioChoferCalculado = salarioChoferCalculado;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
