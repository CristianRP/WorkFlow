package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.FlowProgress;
import com.gruporosul.workflow.xml.ParserXmlFlowProgress;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class  ProgresoFlujoActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private FloatingActionButton fabDetalle;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/WF_Flow_Detail_List?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progreso_flujo);

        if (getIntent().getStringExtra("descripcion") != null && getIntent().getStringExtra("id") != null && getIntent().getStringExtra("agrupador") != null) {
            setToolbar(getIntent().getStringExtra("id") + "/" +
                    getIntent().getStringExtra("descripcion") + "/" +
                    getIntent().getStringExtra("agrupador"));
        } else {
            setToolbar("N/A");
        }

        new TareaDescargaXml().execute(URL + "codFlujo=" + getIntent().getStringExtra("codFlujo") +
                "&agrupador=" + getIntent().getStringExtra("agrupador").replace(" ", "%20") +
                "&correlativo=" + getIntent().getStringExtra("correlativo") + "&estado=" + getIntent().getStringExtra("estado"));

        mProgressDialog = new ProgressDialog(ProgresoFlujoActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        fabDetalle = (FloatingActionButton) findViewById(R.id.detallePasos);
        fabDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detalle = new Intent(ProgresoFlujoActivity.this, FlowDetailActivity.class);
                detalle.putExtra("correlativo", getIntent().getStringExtra("correlativo"));
                detalle.putExtra("id", getIntent().getStringExtra("id"));
                detalle.putExtra("descripcion", getIntent().getStringExtra("descripcion"));
                detalle.putExtra("agrupador", getIntent().getStringExtra("agrupador"));
                startActivity(detalle);
            }
        });

    }

    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar(String title) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(title);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Establecer icono del drawer toggle
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private class TareaDescargaXml extends AsyncTask<String, Void, List<FlowProgress>> {

        @Override
        protected List<FlowProgress> doInBackground(String... urls) {
            try {
                Log.e("hao", urls[0]);
                return parsearXmlDeUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }
  
        @Override
        protected void onPostExecute(List<FlowProgress> items) {

            FlowProgress.FLUJO = items;

            setData(getIntent().getStringExtra("correlativo"));
            mProgressDialog.dismiss();

        }

        private List<FlowProgress> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlFlowProgress mParserXmlFlowProgress = new ParserXmlFlowProgress();
            List<FlowProgress> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlFlowProgress.parsear(mInputStream);

            } finally {
                if (mInputStream != null) {
                    mInputStream.close();
                }
            }

            return items;

        }

        private InputStream descargarContenido(String urlString)
                throws IOException {

            java.net.URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(25000);
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");
            connection.connect();

            return connection.getInputStream();

        }

    }

    public void setData(String correlativo) {

        FlowProgress flow = FlowProgress.getItem(correlativo);


        StackedBarChart mStackedBarChart = (StackedBarChart) findViewById(R.id.stackedbarchart);
        TextView txtCorrelativo = (TextView) findViewById(R.id.txtCorrelativo);
        TextView txtId = (TextView) findViewById(R.id.txtIdentificador);
        TextView txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        TextView txtCodFlujo = (TextView) findViewById(R.id.txtCodFlujo);
        TextView txtAgrupador = (TextView) findViewById(R.id.txtAgrupador);
        TextView txtTipo = (TextView) findViewById(R.id.txtTipo);
        TextView fechaInicial = (TextView) findViewById(R.id.txtFecha);
        TextView fechaProyectada = (TextView) findViewById(R.id.txtFechaProyectada);
        TextView pasoActual = (TextView) findViewById(R.id.txtPasoActual);
        TextView txtCumplimiento = (TextView) findViewById(R.id.txtCumplimiento);
        TextView pasosActuales = (TextView) findViewById(R.id.txtPasosActuales);

        txtCorrelativo.setText(Html.fromHtml(getString(R.string.correlativo, flow.getCorrelativoFlujo())));
        txtCorrelativo.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtId.setText(Html.fromHtml(getString(R.string.identificador, flow.getId())));
        txtId.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtDescripcion.setText(Html.fromHtml(getString(R.string.descripcion, flow.getDescripcion())));
        txtDescripcion.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtCodFlujo.setText(Html.fromHtml(getString(R.string.codFlujo, flow.getCodFlujo())));
        txtCodFlujo.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtAgrupador.setText(Html.fromHtml(getString(R.string.agrupador, flow.getAgrupador())));
        txtAgrupador.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtTipo.setText(Html.fromHtml(getString(R.string.tipo, flow.getTipo())));
        txtTipo.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        fechaInicial.setText(Html.fromHtml(getString(R.string.fecha, flow.getFecha())));
        fechaInicial.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        fechaProyectada.setText(Html.fromHtml(getString(R.string.fecha_proyectada, flow.getFechaProyectada())));
        fechaProyectada.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        pasoActual.setText(Html.fromHtml(getString(R.string.paso_actual, flow.getPasoActual())));
        pasoActual.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        txtCumplimiento.setText(Html.fromHtml(getString(R.string.cumplimiento, flow.getCumplimiento())));
        txtCumplimiento.setTextColor(getColor(getApplicationContext(), R.color.header_color));
        pasosActuales.setText(Html.fromHtml(getString(R.string.pasos_actuales, flow.getCantPasosLleva())));
        pasosActuales.setTextColor(getColor(getApplicationContext(), R.color.header_color));


        StackedBarModel s1 = new StackedBarModel(flow.getTotalPasos());

        s1.addBar(new BarModel(Float.parseFloat(flow.getCantPasosLleva()), 0xffff5722)); //0xFF63CBB0
        Float diferencia = Float.parseFloat(flow.getTotalPasos()) - Float.parseFloat(flow.getCantPasosLleva());
        s1.addBar(new BarModel(diferencia, 0xff4e342e)); //0xFF56B7F1

        Log.e("cantlleva", "" + Float.parseFloat(flow.getCantPasosLleva()));
        Log.e("total", Float.valueOf(flow.getTotalPasos())+"");

        mStackedBarChart.addBar(s1);

        mStackedBarChart.startAnimation();
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        FlowProgress.FLUJO.clear();
    }

}
