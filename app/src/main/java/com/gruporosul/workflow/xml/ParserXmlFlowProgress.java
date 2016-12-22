package com.gruporosul.workflow.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.workflow.bean.FlowProgress;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 13/11/2015.
 */
public class ParserXmlFlowProgress {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfWF_Flow_Detail_List";
    private static final String ETIQUETA_FLUJOS = "WF_Flow_Detail_List";

    private static final String ETIQUETA_CORRELATIVO = "correlativoFlujo";
    private static final String ETIQUETA_IDFLUJO = "identificador";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_DIAS_ACTUALES = "diasActuales";
    private static final String ETIQUETA_TOTAL_DIAS = "totalDias";
    private static final String ETIQUETA_COD_FLUJO = "codFlujo";
    private static final String ETIQUETA_TIPO = "tipo";
    private static final String ETIQUETA_AGRUPADOR = "agrupador";
    private static final String ETIQUETA_PASO_ACTUAL = "pasoActual";
    private static final String ETIQUETA_CANT_PASO = "cantPasoLleva";
    private static final String ETIQUETA_TOTAL_PASOS = "totalPasos";
    private static final String ETIQUETA_CUMPLIMIENTO = "cumplimiento";
    private static final String ETIQUETA_FECHA_INICIAL = "fecha";
    private static final String ETIQUETA_FECHA_PROYECTADA = "fechaProyectada";
    private static final String ETIQUETA_ESTADO = "estado";
    private static final String ETIQUETA_CORRELATIVO_ACTUAL = "correlativoActual";
    private static final String ETIQUETA_CORRELATIVO_SI = "correlativoSi";
    private static final String ETIQUETA_CORRELATIVO_NO = "correlativoNo";
    private static final String ETIQUETA_SECUENCIA = "secuencia";
    private static final String ETIQUETA_ROLE = "role";
    private static final String ETIQUETA_CORRELATIVO_FIN = "correlativoFin";


    public List<FlowProgress> parsear(InputStream mInputStream)
            throws XmlPullParserException, IOException {

        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(mInputStream, null);
            mXmlPullParser.nextTag();

            return leerFlujos(mXmlPullParser);
        } finally {
            mInputStream.close();
        }

    }

    private List<FlowProgress> leerFlujos(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<FlowProgress> flujos = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = mXmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_FLUJOS)) {
                flujos.add(leerFlujo(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }

        }

        return flujos;

    }

    private FlowProgress leerFlujo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        String correlativo = null;
        String idFlujo = null;
        String descripcion = null;
        String diasActuales = null;
        String totalDias = null;
        String codFlujo = null;
        String agrupador = null;
        String tipo = null;
        String pasoActual = null;
        String cantPasosLleva = null;
        String totalPasos = null;
        String cumplimiento = null;
        String fecha = null;
        String fechaProyectada = null;
        String estado = null;
        String correlativoActual = null;
        String correlativoSi = null;
        String correlativoNo = null;
        String secuencia = null;
        String role = null;
        String correlativoFin = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CORRELATIVO:
                    correlativo = leerCorrelativo(mXmlPullParser);
                    Log.e("correlativo", correlativo);
                    break;
                case ETIQUETA_IDFLUJO:
                    idFlujo = leerIdFlujo(mXmlPullParser);
                    Log.e("idflujo", idFlujo);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(mXmlPullParser);
                    Log.e("descripcion", descripcion);
                    break;
                case ETIQUETA_DIAS_ACTUALES:
                    diasActuales = leerDiasActuales(mXmlPullParser);
                    Log.e("diasActuales", diasActuales);
                    break;
                case ETIQUETA_TOTAL_DIAS:
                    totalDias = leerTotalDias(mXmlPullParser);
                    Log.e("totalDias ", totalDias);
                    break;
                case ETIQUETA_COD_FLUJO:
                    codFlujo = leerCodFlujo(mXmlPullParser);
                    Log.e("codFlujo", codFlujo);
                    break;
                case ETIQUETA_AGRUPADOR:
                    agrupador = leerAgrupador(mXmlPullParser);
                    Log.e("agrupador", agrupador);
                    break;
                case ETIQUETA_TIPO:
                    tipo = leerTipo(mXmlPullParser);
                    Log.e("tipo", tipo);
                    break;
                case ETIQUETA_PASO_ACTUAL:
                    pasoActual = leerPasoActual(mXmlPullParser);
                    Log.e("pasoActual", pasoActual);
                    break;
                case ETIQUETA_CANT_PASO:
                    cantPasosLleva = leerCantPasosLleva(mXmlPullParser);
                    Log.e("pasos_lleva", cantPasosLleva);
                    break;
                case ETIQUETA_TOTAL_PASOS:
                    totalPasos = leerTotalPasos(mXmlPullParser);
                    Log.e("total_pasos", totalPasos);
                    break;
                case ETIQUETA_CUMPLIMIENTO:
                    cumplimiento = leerCumplimiento(mXmlPullParser);
                    Log.e("cumplimiento", cumplimiento);
                    break;
                case ETIQUETA_FECHA_INICIAL:
                    fecha = leerFechaInicial(mXmlPullParser);
                    Log.e("fechaInicial", fecha);
                    break;
                case ETIQUETA_FECHA_PROYECTADA:
                    fechaProyectada = leerFechaProyectada(mXmlPullParser);
                    Log.e("fechaProyectada", fechaProyectada );
                    break;
                case ETIQUETA_ESTADO:
                    estado = leerEstado(mXmlPullParser);
                    Log.e("estado", estado );
                    break;
                case ETIQUETA_CORRELATIVO_ACTUAL:
                    correlativoActual = leerCorrelativoActual(mXmlPullParser);
                    break;
                case ETIQUETA_CORRELATIVO_SI:
                    correlativoSi = leerCorrelativoSi(mXmlPullParser);
                    break;
                case ETIQUETA_CORRELATIVO_NO:
                    correlativoNo = leerCorrelativoNo(mXmlPullParser);
                    break;
                case ETIQUETA_SECUENCIA:
                    secuencia = leerSecuencia(mXmlPullParser);
                    Log.e("secuencia", secuencia );
                    break;
                case ETIQUETA_ROLE:
                    role = leerRole(mXmlPullParser);
                    break;
                case ETIQUETA_CORRELATIVO_FIN:
                    correlativoFin = leerCorrelativoFin(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new FlowProgress(
                correlativo,
                idFlujo,
                descripcion,
                diasActuales,
                totalDias,
                codFlujo,
                agrupador,
                tipo,
                pasoActual,
                cantPasosLleva,
                totalPasos,
                fecha,
                fechaProyectada,
                estado,
                cumplimiento,
                correlativoActual,
                correlativoSi,
                correlativoNo,
                secuencia,
                role,
                correlativoFin);

    }

    private String leerCorrelativo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO);
        String correlativo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO);

        return correlativo;

    }

    private String leerIdFlujo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_IDFLUJO);
        String idFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_IDFLUJO);

        return idFlujo;

    }

    private String leerDescripcion(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String idFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);

        return idFlujo;

    }

    private String leerPasoActual(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PASO_ACTUAL);
        String pasoActual = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PASO_ACTUAL);

        return pasoActual;

    }

    private String leerCumplimiento(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CUMPLIMIENTO);
        String cumplimiento = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CUMPLIMIENTO);

        return cumplimiento;

    }

    private String leerFechaInicial(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_INICIAL);
        String fechaInicial = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_INICIAL);

        return fechaInicial;

    }

    private String leerFechaProyectada(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_PROYECTADA);
        String fechaProyectada = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_PROYECTADA);

        return fechaProyectada;

    }

    private String leerDiasActuales(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DIAS_ACTUALES);
        String diasActuales = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DIAS_ACTUALES);

        return diasActuales;

    }

    private String leerTotalDias(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TOTAL_DIAS);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TOTAL_DIAS);

        return totalDias;

    }

    private String leerCodFlujo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_FLUJO);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_FLUJO);

        return totalDias;

    }

    private String leerAgrupador(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_AGRUPADOR);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_AGRUPADOR);

        return totalDias;

    }

    private String leerTipo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO);

        return totalDias;

    }

    private String leerCantPasosLleva(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CANT_PASO);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CANT_PASO);

        return totalDias;

    }

    private String leerTotalPasos(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TOTAL_PASOS);
        String totalDias = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TOTAL_PASOS);

        return totalDias;

    }

    private String leerEstado(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ESTADO);
        String estado = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ESTADO);
        return estado;
    }

    private String leerCorrelativoActual(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_ACTUAL);
        String correlativoActual = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_ACTUAL);
        return correlativoActual;
    }

    private String leerCorrelativoSi(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_SI);
        String correlativoSi = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_SI);
        return correlativoSi;
    }

    private String leerCorrelativoNo(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_NO);
        String correlativoNo = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_NO);
        return correlativoNo;
    }

    private String leerSecuencia(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_SECUENCIA);
        String secuencia = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_SECUENCIA);
        return secuencia;
    }

    private String leerRole(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ROLE);
        String role = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ROLE);
        return role;
    }

    private String leerCorrelativoFin(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_FIN);
        String fin = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_FIN);
        return fin;
    }

    private String obtenerTexto(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        String resultado = "";

        if (mXmlPullParser.next() == XmlPullParser.TEXT) {
            resultado = mXmlPullParser.getText();
            mXmlPullParser.nextTag();
        }

        return resultado;

    }

    private void saltarEtiqueta(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (mXmlPullParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;

            }
        }

    }

}
