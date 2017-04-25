package com.gruporosul.workflow.xml;

import android.util.Xml;

import com.gruporosul.workflow.bean.ComodinesSiNo;

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


public class ParserXmlComodinesSiNo {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfComodinesSiNo";
    private static final String ETIQUETA_FLUJOS = "ComodinesSiNo";

    private static final String ETIQUETA_COMODINES_SI = "comodinesSi";
    private static final String ETIQUETA_COMODINES_NO = "comodinesNo";

    public List<ComodinesSiNo> parsear(InputStream mInputStream)
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

    private List<ComodinesSiNo> leerComodines(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<ComodinesSiNo> flujos = new ArrayList<>();

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

    private ComodinesSiNo leerComodin(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        int comodinSi = 0;
        int comodinNo = 0;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_COMODINES_SI:
                    comodinSi = leerComodinSi(mXmlPullParser);
                    break;
                case ETIQUETA_COMODINES_NO:
                    comodinNo = leerComodinNo(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new ComodinesSiNo(
                comodinSi,
                comodinNo
        );

    }

    private int leerComodinSi(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COMODINES_SI);
        int si = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COMODINES_SI);
        return si;
    }

    private int leerComodinNo(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COMODINES_NO);
        int no = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COMODINES_NO);
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
