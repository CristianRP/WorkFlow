package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 17/11/2015.
 */
public class Agrupador {

    private String codFlujo;
    private String agrupador;

    public static List<Agrupador> AGRUPADORES = new ArrayList<>();

    public static Agrupador getAgrupador(String id) {
        for (Agrupador agrupador : AGRUPADORES) {
            if (agrupador.getCodFlujo().equals(id)) {
                return agrupador;
            }
        }
        return null;
    }

    public Agrupador() {
    }

    public Agrupador(String codFlujo, String agrupador) {
        this.codFlujo = codFlujo;
        this.agrupador = agrupador;
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
}
