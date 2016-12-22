package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Comodin;
import com.gruporosul.workflow.preferences.PrefManager;
import com.gruporosul.workflow.volley.AppController;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComodinActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    @BindString(R.string.title_activity_comodin)
    String titleActivity;
    @BindView(R.id.content_comodin)
    LinearLayout mContentComodin;
    @BindView(R.id.titlePasoActual)
    TextView titlePasoActual;

    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;
    private static final String wf_insert_ctrl_flujo_comodin =
            "http://200.30.160.117:8070/Servicioclientes.asmx/wf_insert_ctrl_flujo_comodin";

    private static final String update_ctrl_flujo = "http://200.30.160.117:8070/Servicioclientes.asmx/wf_update_ctrl_flujo";

    private static final String insert_ctrl_flujo = "http://200.30.160.117:8070/ServicioClientes.asmx/wf_insert_ctr_flujo";


    private String correlativoFlujo;
    private String correlativoComodin;
    private String correlativoSiguiente;
    private String secuencia;
    private String usuario;
    private String tipo;
    private String fecha;
    List<TextInputLayout> allInputEditText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodin);

        ButterKnife.bind(this);

        setToolbar();

        mPrefManager = new PrefManager(this);

        titlePasoActual.setText(getIntent().getStringExtra("pasoActual"));

        for (int i = 0; i < Comodin.LISTA_COMODINES.size(); i++) {
            TextInputLayout txtInput = createTextInputLayouts(Comodin.LISTA_COMODINES.get(i).getComodin(),
                    Comodin.LISTA_COMODINES.get(i).getTipo());
            Log.e("tipos", Comodin.LISTA_COMODINES.get(i).getTipo());
            /*if (Comodin.LISTA_COMODINES.get(i).getTipo().toLowerCase().equals("date")) {
                createBottomSheet();
            }*/
            allInputEditText.add(txtInput);
            mContentComodin.addView(txtInput);
        }

        Intent comodin = getIntent();
        correlativoFlujo = comodin.getStringExtra("correlativoFlujo");
        correlativoComodin = comodin.getStringExtra("correlativo");
        secuencia = comodin.getStringExtra("secuencia");
        correlativoSiguiente = comodin.getStringExtra("correlativoSiguiente");
        usuario = mPrefManager.getKeyUser();
        tipo = comodin.getStringExtra("tipo");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comodin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.sendComodin:
                String[] values = new String[allInputEditText.size()];
                for(int i=0; i < allInputEditText.size(); i++){
                    values[i] = allInputEditText.get(i).getEditText().getText().toString();
                }
                for (int i = 0; i < Comodin.LISTA_COMODINES.size(); i++) {
                    Log.e("valores", values[i]);
                    showDialogInsertComodin(correlativoFlujo, correlativoComodin,
                            secuencia, Comodin.LISTA_COMODINES.get(i).getComodin().replace(":", ""), values[i],
                            Comodin.LISTA_COMODINES.get(i).getAfecta(), usuario);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(titleActivity);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private TextInputLayout createTextInputLayouts(String content, String tipo) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.addView(createTextInputEditTexts(content, tipo));
        return textInputLayout;
    }

    private TextInputEditText createTextInputEditTexts(String content, String tipo) {
        TextInputEditText textInputEditText = new TextInputEditText(this);
        textInputEditText.setHint(content);
        textInputEditText.setTag(content);
        switch (tipo.toLowerCase()) {
            case "number":
                textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "varchar2":
                textInputEditText.setMinLines(2);
                textInputEditText.setMaxLines(5);
                Log.e(":v", "tipo varchar2");
                textInputEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case "date":
                textInputEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
                textInputEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createBottomSheet();
                    }
                });
                break;
            default:
                break;
        }
        return textInputEditText;
    }

    private BottomSheetDatePickerDialog createBottomSheet() {
        Calendar now = Calendar.getInstance();
        BottomSheetDatePickerDialog dialog = BottomSheetDatePickerDialog.newInstance(
                ComodinActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        Calendar max = Calendar.getInstance();
        max.add(Calendar.YEAR, 10);
        dialog.setMaxDate(max);
        dialog.setYearRange(1970, 2032);
        dialog.show(getSupportFragmentManager(), ComodinActivity.class.getSimpleName());
        return dialog;
    }

    private void showDialogInsertComodin(final String correlativoFlujo, final String correlativoComodin,
                                         final String secuencia, final String comodin, final String valor,
                                         final String afecta, final String usuario) {
        new MaterialDialog.Builder(this)
                .title("Enviar comodines")
                .content("¿Esta seguro de enviar los comodines y dar avance al flujo?")
                .positiveText("Enviar")
                .negativeText("No enviar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mProgressDialog = new ProgressDialog(ComodinActivity.this);
                        mProgressDialog.setMessage("Enviando datos...");
                        mProgressDialog.setCancelable(false);
                        //mProgressDialog.show();
                        Toast.makeText(ComodinActivity.this, fecha, Toast.LENGTH_SHORT).show();
                        /*insertComodin(correlativoFlujo, correlativoComodin, secuencia, comodin,
                                valor, afecta, usuario);*/
                        Log.e("values", correlativoFlujo+ correlativoComodin+ secuencia+ comodin+
                                valor+ afecta+ usuario);
                    }
                })
                .show();
    }

    private void insertComodin(final String correlativoFlujo, final String correlativoComodin,
                               final String secuencia, final String comodin, final String valor,
                               final String afecta, final String usuario) {

        StringRequest insertComodin = new StringRequest(
                Request.Method.POST,
                wf_insert_ctrl_flujo_comodin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("actaulizado", "actualizdado con exito");
                        updateStatusControlFlujo(correlativoFlujo, correlativoComodin, "P");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("correlativoFlujo", correlativoFlujo);
                params.put("correlativo", correlativoComodin);
                params.put("secuencia", secuencia);
                params.put("comodin", comodin);
                params.put("valor", valor);
                params.put("afecta", afecta);
                params.put("usuario", usuario);
                return params;
            }
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                String json;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    try {
                        json = new String(volleyError.networkResponse.data,
                                HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                    } catch (UnsupportedEncodingException e) {
                        return new VolleyError(e.getMessage());
                    }
                    return new VolleyError(json);
                }
                return volleyError;
            }
        };
        insertComodin.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(insertComodin);
    }

    public void updateStatusControlFlujo(final String correlativoFlujo, final String correlativo, final String estado) {
        StringRequest updateStatus = new StringRequest(
                Request.Method.POST,
                update_ctrl_flujo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("actaulizado", "actualizdado con exito");
                        mProgressDialog.setCancelable(true);
                        Log.e("insert_flujo", correlativoFlujo + correlativoSiguiente + secuencia + tipo);
                        insertControlFlujo(correlativoFlujo, correlativoSiguiente, "A",
                                mPrefManager.getKeyUser(), secuencia, tipo.substring(1,32));
                        if (tipo.length() <= 32) {
                            insertControlFlujo(correlativoFlujo, correlativoSiguiente, "A",
                                    mPrefManager.getKeyUser(), secuencia, tipo);
                        } else {
                            insertControlFlujo(correlativoFlujo, correlativoSiguiente, "A",
                                    mPrefManager.getKeyUser(), secuencia, tipo.substring(1,32));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("update", correlativoFlujo + " " +  correlativo + " " + estado);
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
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                String json;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    try {
                        json = new String(volleyError.networkResponse.data,
                                HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                    } catch (UnsupportedEncodingException e) {
                        return new VolleyError(e.getMessage());
                    }
                    return new VolleyError(json);
                }
                return volleyError;
            }
        };
        updateStatus.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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
                        Log.e("insert_control", "success" + response);
                        Toast.makeText(ComodinActivity.this, "Datos ingresados con éxito!", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        MainActivity.mainActivity.finish();
                        AgrupadorActivity.agrupadorActivity.finish();
                        FlujosActivity.flujosActivity.finish();
                        ProgresoFlujoActivity.progresoFlujoActivity.finish();
                        startActivity(new Intent(ComodinActivity.this, MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("insert_control", "fail");
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
        insertControl.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(insertControl);
    }

    /**
     * @param dialog
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        fecha = DateFormat.getDateFormat(this).format(cal.getTime());
        for (TextInputLayout tx : allInputEditText) {
            if (tx.getEditText().getInputType() == InputType.TYPE_CLASS_DATETIME) {
                tx.getEditText().setText(fecha);
            }
        }
        Toast.makeText(this, fecha, Toast.LENGTH_SHORT).show();
    }
}
