package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Comodin;
import com.gruporosul.workflow.bean.FlowProgress;
import com.gruporosul.workflow.preferences.PrefManager;
import com.gruporosul.workflow.volley.AppController;
import com.gruporosul.workflow.xml.ParserXmlComodin;
import com.gruporosul.workflow.xml.ParserXmlFlowProgress;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  ProgresoFlujoActivity extends AppCompatActivity {

    @BindString(R.string.dialog_next_step)
    String titleDialog;
    @BindArray(R.array.items_dialog_next_step)
    String[] itemsDialog;
    @BindString(R.string.dialog_op_aceptar)
    String strAceptar;
    @BindString(R.string.dialog_op_cancelar)
    String strCancelar;
    @BindString(R.string.dialog_op_default)
    String strDefault;
    @BindView(R.id.coordinatorProgresoFlujo)
    CoordinatorLayout mCoordinatorProgresoFlujo;

    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/WF_Flow_Detail_List?";

    private static final String get_comodines =
            "http://200.30.160.117:8070/Servicioclientes.asmx/wf_get_prototipo_comodin?correlativo=%s&afecta=%s";

    private static final String update_ctrl_flujo = "http://200.30.160.117:8070/Servicioclientes.asmx/wf_update_ctrl_flujo";

    private static final String insert_ctrl_flujo = "http://200.30.160.117:8070/ServicioClientes.asmx/wf_insert_ctr_flujo";

    private String correaltivoFlujo;
    private String correlativoActual;
    private String correaltivoSiguiente;
    private String afecta;
    private String secuencia;
    private String tipoFlujo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progreso_flujo);

        ButterKnife.bind(this);

        mPrefManager = new PrefManager(this);

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

    }

    @OnClick(R.id.fabHistorico)
    void showHistoryOfWF() {
        Intent detalle = new Intent(ProgresoFlujoActivity.this, FlowDetailActivity.class);
        detalle.putExtra("correlativo", getIntent().getStringExtra("correlativo"));
        detalle.putExtra("id", getIntent().getStringExtra("id"));
        detalle.putExtra("descripcion", getIntent().getStringExtra("descripcion"));
        detalle.putExtra("agrupador", getIntent().getStringExtra("agrupador"));
        startActivity(detalle);
    }

    @OnClick(R.id.fabSeguimiento)
    void showNextStep() {
        showNextStepDialog();
    }

    private void showNextStepDialog() {
        new MaterialDialog.Builder(this)
                .title(titleDialog)
                .items(itemsDialog)
                .positiveText(strAceptar)
                .negativeText(strCancelar)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                FlowProgress flow = FlowProgress.getItem(getIntent().getStringExtra("correlativo"));
                                new GetComodines().execute(String.format(get_comodines, flow.getCorrelativoActual(),
                                        text));
                                correaltivoFlujo = flow.getCorrelativoFlujo();
                                correlativoActual = flow.getCorrelativoActual();

                                afecta = text.toString();
                                if (afecta.toLowerCase().equals("si")) {
                                    correaltivoSiguiente = flow.getCorrelativoSi();
                                } else {
                                    correaltivoSiguiente = flow.getCorrelativoNo();
                                }
                                Log.e("CORRELATIVO_SIGUIENTE", correaltivoSiguiente);
                                secuencia = flow.getSecuencia();
                                tipoFlujo = flow.getTipo();
                                Toast.makeText(ProgresoFlujoActivity.this, ":v op 1" + text, Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(ProgresoFlujoActivity.this, ":v op 2", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Snackbar.make(mCoordinatorProgresoFlujo, strDefault, Snackbar.LENGTH_LONG).show();
                                break;
                        }
                        return true;
                    }
                })
                .show();
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

    private class GetComodines extends AsyncTask<String, Void, List<Comodin>> {

        @Override
        protected List<Comodin> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Comodin> items) {

            Comodin.LISTA_COMODINES = items;

            mProgressDialog.dismiss();
            
            if (Comodin.LISTA_COMODINES.size() <= 0) {
                Toast.makeText(ProgresoFlujoActivity.this, "No tiene comodines, se procedera a avanzar el paso", Toast.LENGTH_SHORT).show();
                mProgressDialog = new ProgressDialog(ProgresoFlujoActivity.this);
                mProgressDialog.setMessage("Cargando...");
                //mProgressDialog.show();
                updateStatusControlFlujo(correaltivoFlujo, correlativoActual, "P");
            } else {
                Intent comodines = new Intent(ProgresoFlujoActivity.this, ComodinActivity.class);
                try {
                    comodines.putExtra("correlativo", correlativoActual);
                    comodines.putExtra("afecta", afecta);
                    startActivity(comodines);
                } catch (NullPointerException npe) {
                    Toast.makeText(ProgresoFlujoActivity.this, "No se pudo encontrar el correlativo actual", Toast.LENGTH_SHORT).show();
                }
            }


        }

        private List<Comodin> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlComodin mParserXmlComodin = new ParserXmlComodin();
            List<Comodin> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlComodin.parsear(mInputStream);

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

    public void updateStatusControlFlujo(final String correlativoFlujo, final String correlativo, final String estado) {
        StringRequest updateStatus = new StringRequest(
                Request.Method.POST,
                update_ctrl_flujo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("actaulizado", "actualizdado con exito");
                        insertControlFlujo(correaltivoFlujo, correaltivoSiguiente, "A",
                                mPrefManager.getKeyUser(), secuencia, tipoFlujo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("insert_control", "fail con exito" + error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("correlativoFlujo", correlativoFlujo);
                params.put("correlativo", correlativo);
                params.put("estado", estado);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(updateStatus);
    }

    public void insertControlFlujo(final String correlativoFlujo, final String correlativoComodin,
                                    final String estado, final String usuario, final String secuencia,
                                    final String tipoFlujo) {
        StringRequest insertControl = new StringRequest(
                Request.Method.POST,
                insert_ctrl_flujo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("insert_control", "Ingresado con exito");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("insert_control", "fail " + error.getMessage().toLowerCase().toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("correlativoFlujo", correlativoFlujo);
                params.put("correlativoComodin", correlativoComodin);
                params.put("estado", estado);
                params.put("usuario", usuario);
                params.put("secuencia", secuencia);
                params.put("tipoFlujo", tipoFlujo);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(insertControl);
    }

}
