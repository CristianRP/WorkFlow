package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/12/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ComodinesSiNo {

    private int comodinesSi;
    private int comodinesNo;

    public ComodinesSiNo() {

    }

    public static List<ComodinesSiNo> COMODINES_SI_NO = new ArrayList<>();

    public ComodinesSiNo(int comodinesSi, int comodinesNo) {
        this.comodinesSi = comodinesSi;
        this.comodinesNo = comodinesNo;
    }

    public int getComodinesSi() {
        return comodinesSi;
    }

    public void setComodinesSi(int comodinesSi) {
        this.comodinesSi = comodinesSi;
    }

    public int getComodinesNo() {
        return comodinesNo;
    }

    public void setComodinesNo(int comodinesNo) {
        this.comodinesNo = comodinesNo;
    }
}
