package com.gruporosul.workflow.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.workflow.bean.FlowDetail;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 13/11/2015.
 */
public class ParserXmlFlowDetail {

    private static final String ns = null;

    private static final String ETIQUETA_ARRAY = "ArrayOfWF_Flow_History";
    private static final String ETIQUETA_FLUJOS = "WF_Flow_History";

    private static final String ETIQUETA_CORRELATIVO = "correlativo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_FECHA_INICIAL = "fechaInicial";
    private static final String ETIQUETA_FECHA_FINAL = "fechaFinal";
    private static final String ETIQUETA_DIAS_EJECUTADOS = "ejecutadoDias";
    private static final String ETIQUETA_DURACION_PRES = "duracionPresupuestada";
    private static final String ETIQUETA_ESTADO = "estado";
    private static final String ETIQUETA_USUARIO = "usuario";
    private static final String ETIQUETA_COD_FLUJO = "codFlujo";

    public List<FlowDetail> parsear(InputStream mInputStream)
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

    private List<FlowDetail> leerFlujos(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<FlowDetail> flujos = new ArrayList<>();

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

    private FlowDetail leerFlujo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FLUJOS);

        String correlativo = null;
        String descripcion = null;
        String fechaInicial = null;
        String fechaFinal = null;
        String diasEjecutados = null;
        String duracionPres = null;
        String estado = null;
        String usuario = null;
        String codFlujo = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {

            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = mXmlPullParser.getName();

            switch (name) {
                case ETIQUETA_CORRELATIVO:
                    correlativo = leerCorrelativo(mXmlPullParser);
                    Log.e("idflujo", correlativo);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(mXmlPullParser);
                    Log.e("pasoActual", descripcion);
                    break;
                case ETIQUETA_FECHA_INICIAL:
                    fechaInicial = leerFechaInicial(mXmlPullParser);
                    Log.e("cumplimiento", fechaInicial);
                    break;
                case ETIQUETA_FECHA_FINAL:
                    fechaFinal = leerFechaFinal(mXmlPullParser);
                    Log.e("fechaInicial", fechaFinal);
                    break;
                case ETIQUETA_DIAS_EJECUTADOS:
                    diasEjecutados = leerDiasEjecutados(mXmlPullParser);
                    Log.e("fechaProyectada", diasEjecutados );
                    break;
                case ETIQUETA_DURACION_PRES:
                    duracionPres = leerDuracionPres(mXmlPullParser);
                    Log.e("diasActuales", duracionPres);
                    break;
                case ETIQUETA_ESTADO:
                    estado = leerEstado(mXmlPullParser);
                    Log.e("totalDias ", estado);
                    break;
                case ETIQUETA_USUARIO:
                    usuario = leerUsuario(mXmlPullParser);
                    Log.e("usuario", usuario);
                    break;
                case ETIQUETA_COD_FLUJO:
                    codFlujo = leerCodFlujo(mXmlPullParser);
                    Log.e("codFlujo", codFlujo);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;

            }

        }

        return new FlowDetail(
                correlativo,
                descripcion,
                fechaInicial,
                fechaFinal,
                diasEjecutados,
                duracionPres,
                estado,
                usuario,
                codFlujo
        );

    }

    private String leerCorrelativo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CORRELATIVO);
        String correlativo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CORRELATIVO);

        return correlativo;

    }

    private String leerDescripcion(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String descripcion = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);

        return descripcion;

    }

    private String leerFechaInicial(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_INICIAL);
        String fechaInicial = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_INICIAL);

        return fechaInicial;

    }

    private String leerFechaFinal(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_FINAL);
        String fechaFinal = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_FINAL);

        return fechaFinal;

    }

    private String leerDiasEjecutados(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DIAS_EJECUTADOS);
        String diasEje = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DIAS_EJECUTADOS);

        return diasEje;

    }

    private String leerDuracionPres(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DURACION_PRES);
        String duracion = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DURACION_PRES);

        return duracion;

    }

    private String leerEstado(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ESTADO);
        String estado = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ESTADO);

        return estado;

    }

    private String leerUsuario(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO);
        String usuario = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO);

        return usuario;

    }

    private String leerCodFlujo(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_FLUJO);
        String codFlujo = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_FLUJO);

        return codFlujo;

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
