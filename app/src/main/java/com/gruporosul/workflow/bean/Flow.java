package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 12/11/2015.
 */
public class Flow {

    private String correlativoFlujo;
    private String idFlujo;
    private String descripcion;
    private String diasActuales;
    private String totalDias;
    private String cumplimiento;

    public static List<Flow> FLUJOS = new ArrayList<>();

    public static Flow getFlujo(String id) {
        for (Flow flow : FLUJOS) {
            if (flow.getCorrelativoFlujo().equals(id)) {
                return flow;
            }
        }
        return null;
    }

    public Flow() {
    }


    public Flow(String correlativoFlujo, String idFlujo, String descripcion, String diasActuales, String totalDias, String cumplimiento) {
        this.correlativoFlujo = correlativoFlujo;
        this.idFlujo = idFlujo;
        this.descripcion = descripcion;
        this.diasActuales = diasActuales;
        this.totalDias = totalDias;
        this.cumplimiento = cumplimiento;
    }

    public String getCorrelativoFlujo() {
        return correlativoFlujo;
    }

    public void setCorrelativoFlujo(String correlativoFlujo) {
        this.correlativoFlujo = correlativoFlujo;
    }

    public String getIdFlujo() {
        return idFlujo;
    }

    public void setIdFlujo(String idFlujo) {
        this.idFlujo = idFlujo;
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

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }
}
