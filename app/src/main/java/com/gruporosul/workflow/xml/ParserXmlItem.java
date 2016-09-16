package com.gruporosul.workflow.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.workflow.bean.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 11/11/2015.
 */
public class ParserXmlItem {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfWF_Type_List";
    private static final String ETIQUETA_TIPOS = "WF_Type_List";

    private static final String ETIQUETA_CODIGO = "codigo";
    private static final String ETIQUETA_TIPO = "tipo";
    private static final String ETIQUETA_IMAGEN = "imagen";

    public List<Item> parsear(InputStream mInputStream)
        throws XmlPullParserException, IOException {

        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(mInputStream, null);
            mXmlPullParser.nextTag();
            return leerTipos(mXmlPullParser);
        } finally {
            mInputStream.close();
        }

    }

    private List<Item> leerTipos(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        List<Item> items = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = mXmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_TIPOS)) {
                items.add(leerItem(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }

        }

        return items;

    }

    private Item leerItem(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPOS);

        int codigo = 0;
        String tipo = null;
        String imagen = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CODIGO:
                    codigo = leerCodigo(mXmlPullParser);
                    Log.e("codigo", codigo+ "");
                    break;
                case ETIQUETA_TIPO:
                    tipo = leerTipo(mXmlPullParser);
                    Log.e("codigo", tipo);
                    break;
                case ETIQUETA_IMAGEN:
                    imagen = leerImagen(mXmlPullParser);
                    Log.e("codigo", imagen);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;
            }

        }

        return new Item(codigo,
                imagen, tipo);

    }

    private int leerCodigo(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CODIGO);
        int codigo = Integer.parseInt(obtenerTexto(mXmlPullParser));
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CODIGO);

        return codigo;

    }

    private String leerTipo(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO);
        String tipo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO);

        return tipo;

    }

    private String leerImagen(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_IMAGEN);
        String imagen = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_IMAGEN);

        return imagen;

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
