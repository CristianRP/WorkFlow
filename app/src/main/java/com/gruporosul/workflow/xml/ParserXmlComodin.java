package com.gruporosul.workflow.xml;

import android.util.Xml;

import com.gruporosul.workflow.bean.Comodin;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 28/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlComodin {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfComodin";
    private static final String ETIQUETA_FLUJOS = "Comodin";

    private static final String ETIQUETA_CORRELATIVO = "correlativo";
    private static final String ETIQUETA_COMODIN = "comodin";
    private static final String ETIQUETA_TIPO = "tipo";
    private static final String ETIQUETA_AFECTA = "afecta";

    public List<Comodin> parsear(InputStream mInputStream)
            throws XmlPullParserException, IOException {

        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(mInputStream, null);
            mXmlPullParser.nextTag();

            return leerComodines(mXmlPullParser);
        } finally {
            mInputStream.close();
        }

    }

    private List<Comodin> leerComodines(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<Comodin> comodins = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = mXmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_FLUJOS)) {
                comodins.add(leerComodin(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }

        }

        return comodins;
    }

    private Comodin leerComodin(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        int correlativo = 0;
        String comodin = null;
        String tipo = null;
        String afecta = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CORRELATIVO:
                    correlativo = leerCorrelativo(mXmlPullParser);
                    break;
                case ETIQUETA_COMODIN:
                    comodin = leerComodinD(mXmlPullParser);
                    break;
                case ETIQUETA_TIPO:
                    tipo = leerTipo(mXmlPullParser);
                    break;
                case ETIQUETA_AFECTA:
                    afecta = leerAfecta(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new Comodin(
                correlativo,
                comodin,
                tipo,
                afecta);

    }


    private int leerCorrelativo(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO);
        int correlativo = Integer.parseInt(obtenerTexto(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO);
        return correlativo;
    }

    private String leerComodinD(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COMODIN);
        String comodin = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COMODIN);
        return comodin;
    }

    private String leerTipo(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO);
        String tipo = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO);
        return tipo;
    }

    private String leerAfecta(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_AFECTA);
        String afecta = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_AFECTA);
        return afecta;
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
