package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 13/11/2015.
 */
public class FlowDetail {

    private String correlativo;
    private String descripcion;
    private String fechaInicial;
    private String fechaFinal;
    private String ejectuadoDias;
    private String duracionPresupuestada;
    private String estado;
    private String usuario;
    private String codFlujo;

    public static List<FlowDetail> DETALLE = new ArrayList<>();

    public static FlowDetail getItem(String idFlujo) {
        for (FlowDetail item : DETALLE) {
            if (item.getCorrelativo().equals(idFlujo)) {
                return item;
            }
        }
        return null;
    }

    public FlowDetail() {
    }

    public FlowDetail(String correlativo, String descripcion, String fechaInicial,
                      String fechaFinal, String ejectuadoDias, String duracionPresupuestada,
                      String estado, String usuario, String codFlujo) {
        this.correlativo = correlativo;
        this.descripcion = descripcion;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.ejectuadoDias = ejectuadoDias;
        this.duracionPresupuestada = duracionPresupuestada;
        this.estado = estado;
        this.usuario = usuario;
        this.codFlujo = codFlujo;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getEjectuadoDias() {
        return ejectuadoDias;
    }

    public void setEjectuadoDias(String ejectuadoDias) {
        this.ejectuadoDias = ejectuadoDias;
    }

    public String getDuracionPresupuestada() {
        return duracionPresupuestada;
    }

    public void setDuracionPresupuestada(String duracionPresupuestada) {
        this.duracionPresupuestada = duracionPresupuestada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCodFlujo() {
        return codFlujo;
    }

    public void setCodFlujo(String codFlujo) {
        this.codFlujo = codFlujo;
    }
}
