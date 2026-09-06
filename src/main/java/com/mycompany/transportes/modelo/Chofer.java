package com.mycompany.transportes.modelo;

import java.sql.Date;

public class Chofer {

    private String dpi;
    private String foto;
    private String numLicencia;
    private String tipoLicencia;
    private Date fechaVencimientoLicencia;
    private double salarioBaseViaje;
    private String disponibilidad;

    public Chofer() {
    }

    public Chofer(String dpi, String foto, String numLicencia, String tipoLicencia, Date fechaVencimientoLicencia, double salarioBaseViaje) {
        this.dpi = dpi;
        this.foto = foto;
        this.numLicencia = numLicencia;
        this.tipoLicencia = tipoLicencia;
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
        this.salarioBaseViaje = salarioBaseViaje;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNumLicencia() {
        return numLicencia;
    }

    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public Date getFechaVencimientoLicencia() {
        return fechaVencimientoLicencia;
    }

    public void setFechaVencimientoLicencia(Date fechaVencimientoLicencia) {
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
    }

    public double getSalarioBaseViaje() {
        return salarioBaseViaje;
    }

    public void setSalarioBaseViaje(double salarioBaseViaje) {
        this.salarioBaseViaje = salarioBaseViaje;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
