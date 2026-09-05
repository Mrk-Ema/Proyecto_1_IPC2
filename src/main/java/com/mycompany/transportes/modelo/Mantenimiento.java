package com.mycompany.transportes.modelo;

import java.sql.Date;

public class Mantenimiento {

    private int idMantenimiento;
    private int idBus;
    private double montoManoObra;
    private double montoRepuestos;
    private Date fechaMantenimiento;
    private String descripcion;

    public Mantenimiento() {
    }

    public Mantenimiento(int idBus, double montoManoObra, double montoRepuestos, Date fechaMantenimiento, String descripcion) {
        this.idBus = idBus;
        this.montoManoObra = montoManoObra;
        this.montoRepuestos = montoRepuestos;
        this.fechaMantenimiento = fechaMantenimiento;
        this.descripcion = descripcion;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public double getMontoManoObra() {
        return montoManoObra;
    }

    public void setMontoManoObra(double montoManoObra) {
        this.montoManoObra = montoManoObra;
    }

    public double getMontoRepuestos() {
        return montoRepuestos;
    }

    public void setMontoRepuestos(double montoRepuestos) {
        this.montoRepuestos = montoRepuestos;
    }

    public Date getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(Date fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
