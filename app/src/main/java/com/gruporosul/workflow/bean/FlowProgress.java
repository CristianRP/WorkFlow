package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 18/11/2015.
 */
public class FlowProgress {

    private String correlativoFlujo;
    private String id;
    private String descripcion;
    private String diasActuales;
    private String totalDias;
    private String codFlujo;
    private String agrupador;
    private String tipo;
    private String pasoActual;
    private String cantPasosLleva;
    private String totalPasos;
    private String fecha;
    private String fechaProyectada;
    private String cumplimiento;

    public static List<FlowProgress> FLUJO = new ArrayList<>();

    public static FlowProgress getItem(String idFlujo) {
        for (FlowProgress item : FLUJO) {
            if (item.getCorrelativoFlujo().equals(idFlujo)) {
                return item;
            }
        }
        return null;
    }

    public FlowProgress() {
    }

    public FlowProgress(String correlativoFlujo, String id, String descripcion,
                        String diasActuales, String totalDias, String codFlujo,
                        String agrupador, String tipo, String pasoActual, String cantPasosLleva,
                        String totalPasos, String fecha, String fechaProyectada, String cumplimiento) {
        this.correlativoFlujo = correlativoFlujo;
        this.id = id;
        this.descripcion = descripcion;
        this.diasActuales = diasActuales;
        this.totalDias = totalDias;
        this.codFlujo = codFlujo;
        this.agrupador = agrupador;
        this.tipo = tipo;
        this.pasoActual = pasoActual;
        this.cantPasosLleva = cantPasosLleva;
        this.totalPasos = totalPasos;
        this.fecha = fecha;
        this.fechaProyectada = fechaProyectada;
        this.cumplimiento = cumplimiento;
    }

    public String getCorrelativoFlujo() {
        return correlativoFlujo;
    }

    public void setCorrelativoFlujo(String correlativoFlujo) {
        this.correlativoFlujo = correlativoFlujo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDiasActuales() {
        return diasActuales;
    }

    public void setDiasActuales(String diasActuales) {
        this.diasActuales = diasActuales;
    }

    public String getTotalDias() {
        return totalDias;
    }

    public void setTotalDias(String totalDias) {
        this.totalDias = totalDias;
    }

    public String getCodFlujo() {
        return codFlujo;
    }

    public void setCodFlujo(String codFlujo) {
        this.codFlujo = codFlujo;
    }

    public String getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(String agrupador) {
        this.agrupador = agrupador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(String pasoActual) {
        this.pasoActual = pasoActual;
    }

    public String getCantPasosLleva() {
        return cantPasosLleva;
    }

    public void setCantPasosLleva(String cantPasosLleva) {
        this.cantPasosLleva = cantPasosLleva;
    }

    public String getTotalPasos() {
        return totalPasos;
    }

    public void setTotalPasos(String totalPasos) {
        this.totalPasos = totalPasos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaProyectada() {
        return fechaProyectada;
    }

    public void setFechaProyectada(String fechaProyectada) {
        this.fechaProyectada = fechaProyectada;
    }

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }
}
