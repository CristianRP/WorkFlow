package com.gruporosul.workflow.xml;

import android.util.Xml;

import com.gruporosul.workflow.bean.CorrelativoSiNo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/12/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlCorrelativoSiNo {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfCorrelativoSiNo";
    private static final String ETIQUETA_FLUJOS = "CorrelativoSiNo";

    private static final String ETIQUETA_CORRELATIVO_SI = "correlativoSi";
    private static final String ETIQUETA_CORRELATIVO_NO = "correlativoNo";

    public List<CorrelativoSiNo> parsear(InputStream mInputStream)
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

    private List<CorrelativoSiNo> leerComodines(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<CorrelativoSiNo> flujos = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = mXmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_FLUJOS)) {
                flujos.add(leerComodin(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }

        }

        return flujos;

    }

    private CorrelativoSiNo leerComodin(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        int correlativoSi = 0;
        int correlativoNo = 0;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CORRELATIVO_SI:
                    correlativoSi = leercorrelativoSi(mXmlPullParser);
                    break;
                case ETIQUETA_CORRELATIVO_NO:
                    correlativoNo = leercorrelativoNo(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new CorrelativoSiNo(
                correlativoSi,
                correlativoNo
        );

    }

    private int leercorrelativoSi(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_SI);
        int si = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_SI);
        return si;
    }

    private int leercorrelativoNo(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO_NO);
        int no = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO_NO);
        return no;
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
