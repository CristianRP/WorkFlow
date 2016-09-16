package com.gruporosul.workflow.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.workflow.bean.Flow;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 12/11/2015.
 */
public class ParserXmlFlow {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfWF_Flow_List";
    private static final String ETIQUETA_FLUJOS = "WF_Flow_List";

    private static final String ETIQUETA_CORRELATIVO_FLUJO = "correlativoFlujo";
    private static final String ETIQUETA_IDFLUJO = "idFlujo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_DIAS_ACTUALES = "diasActuales";
    private static final String ETIQUETA_TOTAL_DIAS = "totalDias";
    private static final String ETIQUETA_CUMPLIMIENTO = "cumplimiento";

    public List<Flow> parsear(InputStream mInputStream)
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

    private List<Flow> leerFlujos(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        List<Flow> flujos = new ArrayList<>();

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

    private Flow leerFlujo(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        String correlativoFlujo = null;
        String idFlujo = null;
        String descripcion = null;
        String diasActuales = null;
        String totalDias = null;
        String cumplimiento = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CORRELATIVO_FLUJO:
                    correlativoFlujo = leerCorrelativo(mXmlPullParser);
                    Log.e("correlativoFlujo", correlativoFlujo);
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
                    Log.e("totalDias", totalDias);
                    break;
                case ETIQUETA_CUMPLIMIENTO:
                    cumplimiento = leerCumplimiento(mXmlPullParser);
                    Log.e("cumplimiento", cumplimiento);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new Flow(correlativoFlujo,
                idFlujo,
                descripcion,
                diasActuales,
                totalDias,
                cumplimiento);

    }

    private String leerCorrelativo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_FLUJO);
        String codFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_FLUJO);

        return codFlujo;

    }


    private String leerIdFlujo(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_IDFLUJO);
        String idFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_IDFLUJO);

        return idFlujo;

    }

    private String leerDescripcion(XmlPullParser mXmlPullParser)
        throws  XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String descripcion = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);

        return descripcion;

    }

    private String leerCumplimiento(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CUMPLIMIENTO);
        String cumplimiento = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CUMPLIMIENTO);

        return cumplimiento;

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
