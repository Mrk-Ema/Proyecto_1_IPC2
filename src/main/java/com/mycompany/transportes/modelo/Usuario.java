package com.mycompany.transportes.modelo;

public class Usuario {

    private String dpi;
    private String nombreCompleto;
    private String nit;
    private String telefono;
    private String direccion;
    private String correo;
    private String password;
    private String rol;
    private double saldoCartera;
    private int idSucursal;
    private boolean estado;

    public Usuario() {
    }

    public Usuario(String dpi, String nombreCompleto, String nit, String telefono,
            String direccion, String correo, String password, String rol,
            double saldoCartera) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.nit = nit;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.saldoCartera = saldoCartera;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getSaldoCartera() {
        return saldoCartera;
    }

    public void setSaldoCartera(double saldoCartera) {
        this.saldoCartera = saldoCartera;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
