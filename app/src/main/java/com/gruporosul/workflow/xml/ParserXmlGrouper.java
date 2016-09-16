package com.gruporosul.workflow.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.workflow.bean.Agrupador;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 17/11/2015.
 */
public class ParserXmlGrouper {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfWF_Flow_Grouper";
    private static final String ETIQUETA_GROUPER = "WF_Flow_Grouper";

    private static final String ETIQUETA_COD_FLUJO = "codFlujo";
    private static final String ETIQUETA_AGRUPADOR = "agrupador";

    public List<Agrupador> parsear(InputStream mInputStream)
        throws XmlPullParserException, IOException {

        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(mInputStream, null);
            mXmlPullParser.nextTag();
            return leerGroupers(mXmlPullParser);
        } finally {
            mInputStream.close();
        }

    }

    private List<Agrupador> leerGroupers(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        List<Agrupador> agrupadors = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = mXmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_GROUPER)) {
                agrupadors.add(leerGrouper(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }

        }

        return agrupadors;

    }

    private Agrupador leerGrouper(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_GROUPER);

        String codFlujo = null;
        String agrupador = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_COD_FLUJO:
                    codFlujo = leerCodFlujo(mXmlPullParser);
                    Log.e("codFlujo", codFlujo);
                    break;
                case ETIQUETA_AGRUPADOR:
                    agrupador = leerAgrupador(mXmlPullParser);
                    Log.e("agrupador", agrupador);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;
            }

        }

        return new Agrupador(codFlujo, agrupador);

    }

    private String leerCodFlujo(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_FLUJO);
        String codFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_FLUJO);

        return codFlujo;

    }

    private String leerAgrupador(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_AGRUPADOR);
        String nombre = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_AGRUPADOR);

        return nombre;

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
