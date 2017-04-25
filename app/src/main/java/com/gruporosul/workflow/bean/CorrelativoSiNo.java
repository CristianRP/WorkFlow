package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/12/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class CorrelativoSiNo {

    private int correlativoSi;
    private int correlativoNo;

    public CorrelativoSiNo() {

    }

    public static List<CorrelativoSiNo> CORRELATIVO_SI_NO = new ArrayList<>();

    public CorrelativoSiNo(int correlativoSi, int correlativoNo) {
        this.correlativoSi = correlativoSi;
        this.correlativoNo = correlativoNo;
    }

    public int getCorrelativoSi() {
        return correlativoSi;
    }

    public void setCorrelativoSi(int correlativoSi) {
        this.correlativoSi = correlativoSi;
    }

    public int getCorrelativoNo() {
        return correlativoNo;
    }

    public void setCorrelativoNo(int correlativoNo) {
        this.correlativoNo = correlativoNo;
    }
}
