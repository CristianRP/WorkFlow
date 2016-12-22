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
    private String estado;
    private String cumplimiento;
    private String correlativoActual;
    private String correlativoSi;
    private String correlativoNo;
    private String secuencia;
    private String role;
    private String correlativoFin;

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
                        String agrupador, String tipo, String pasoActual,
                        String cantPasosLleva, String totalPasos, String fecha,
                        String fechaProyectada, String estado, String cumplimiento,
                        String correlativoActual, String correlativoSi,
                        String correlativoNo, String secuencia, String role,
                        String correlativoFin) {
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
        this.estado = estado;
        this.cumplimiento = cumplimiento;
        this.correlativoActual = correlativoActual;
        this.correlativoSi = correlativoSi;
        this.correlativoNo = correlativoNo;
        this.secuencia = secuencia;
        this.role = role;
        this.correlativoFin = correlativoFin;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public String getCorrelativoActual() {
        return correlativoActual;
    }

    public void setCorrelativoActual(String correlativoActual) {
        this.correlativoActual = correlativoActual;
    }

    public String getCorrelativoSi() {
        return correlativoSi;
    }

    public void setCorrelativoSi(String correlativoSi) {
        this.correlativoSi = correlativoSi;
    }

    public String getCorrelativoNo() {
        return correlativoNo;
    }

    public void setCorrelativoNo(String correlativoNo) {
        this.correlativoNo = correlativoNo;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCorrelativoFin() {
        return correlativoFin;
    }

    public void setCorrelativoFin(String correlativoFin) {
        this.correlativoFin = correlativoFin;
    }
}
