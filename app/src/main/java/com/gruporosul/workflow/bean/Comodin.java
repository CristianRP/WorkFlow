package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 28/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Comodin {

    private int correlativo;
    private String comodin;
    private String tipo;
    private String afecta;

    public static List<Comodin> LISTA_COMODINES = new ArrayList<>();

    private Comodin getComodin(int correlativo) {
        for (Comodin comodin : LISTA_COMODINES) {
            if (comodin.getCorrelativo() == correlativo) {
                return comodin;
            }
        }
        return null;
    }

    public Comodin() {
    }

    public Comodin(int correlativo, String comodin, String tipo, String afecta) {
        this.correlativo = correlativo;
        this.comodin = comodin;
        this.tipo = tipo;
        this.afecta = afecta;
    }

    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public String getComodin() {
        return comodin;
    }

    public void setComodin(String comodin) {
        this.comodin = comodin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAfecta() {
        return afecta;
    }

    public void setAfecta(String afecta) {
        this.afecta = afecta;
    }
}
