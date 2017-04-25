package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.adapter.FlowTableDataAdapter;
import com.gruporosul.workflow.bean.Flow;
import com.gruporosul.workflow.xml.ParserXmlFlow;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

public class FlujosActivity extends AppCompatActivity {

    private TableView<Flow>  tableFlujos;
    private ProgressDialog mProgressDialog;

    public static FlujosActivity flujosActivity;

    private final static String URL =
            "http://168.234.51.176:8070/Servicioclientes.asmx/WF_Flow_List?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flujos);

        flujosActivity = this;

        if (getIntent().getStringExtra("tipo") != null && getIntent().getStringExtra("estado") != null && getIntent().getStringExtra("agrupador") != null) {
            setToolbar(getIntent().getStringExtra("tipo") + "/" +
                    getIntent().getStringExtra("estado") + "/" +
                    getIntent().getStringExtra("agrupador"));
        } else {
            setToolbar("N/A");
        }

        tableFlujos = (TableView<Flow>) findViewById(R.id.tableFlujos);

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getApplicationContext(), "ID", "Descripción", "Cumplimiento");
        simpleTableHeaderAdapter.setTextSize(15);
        simpleTableHeaderAdapter.setTextColor(getColor(getApplicationContext(), R.color.textColorPrimary));
        tableFlujos.setHeaderAdapter(simpleTableHeaderAdapter);
        //tableFlujos.setHeaderBackground(R.drawable.wood_background);
        tableFlujos.setHeaderBackgroundColor(getColor(getApplicationContext(), R.color.header_color));
        int colorEvenRows = getColor(getApplicationContext(), R.color.windowBackground);
        int colorOddRows = getColor(getApplicationContext(), R.color.row_color);
        tableFlujos.setDataRowColorizer(TableDataRowColorizers.alternatingRows(colorEvenRows, colorOddRows));
        tableFlujos.addDataClickListener(new FlowClickListener());
        tableFlujos.setColumnWeight(0, 2);
        tableFlujos.setColumnWeight(1, 3);
        tableFlujos.setColumnWeight(2, 3);

        new TareaDescargaXml().execute(URL + "codFlujo=" + getIntent().getStringExtra("codFlujo") + "&agrupador=" +
                getIntent().getStringExtra("agrupador").replace(" ", "%20") + "&estado=" + getIntent().getStringExtra("estado"));

        mProgressDialog = new ProgressDialog(FlujosActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

    }

    private class FlowClickListener implements TableDataClickListener<Flow> {

        @Override
        public void onDataClicked(int rowIndex, Flow clickedData) {
            String flujos = clickedData.getCorrelativoFlujo();
            Intent progreso = new Intent(FlujosActivity.this, ProgresoFlujoActivity.class);
            progreso.putExtra("codFlujo", getIntent().getStringExtra("codFlujo"));
            progreso.putExtra("agrupador", getIntent().getStringExtra("agrupador"));
            progreso.putExtra("correlativo", flujos);
            progreso.putExtra("id", clickedData.getIdFlujo());
            progreso.putExtra("descripcion", clickedData.getDescripcion());
            progreso.putExtra("estado", getIntent().getStringExtra("estado"));
            startActivity(progreso);
            //Toast.makeText(FlujosActivity.this, flujos, Toast.LENGTH_SHORT).show();
        }
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

    private class TareaDescargaXml extends AsyncTask<String, Void, List<Flow>> {

        @Override
        protected List<Flow> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Flow> items) {

            Flow.FLUJOS = items;

            tableFlujos.setDataAdapter(new FlowTableDataAdapter(getApplicationContext(), items));
            mProgressDialog.dismiss();

        }

        private List<Flow> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlFlow mParserXmlFlow = new ParserXmlFlow();
            List<Flow> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlFlow.parsear(mInputStream);

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

    /*public Drawable imagen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(R.drawable.wood_background, getApplicationContext().getTheme());
        } else {
            return ContextCompat.getDrawable(getApplicationContext(), R.drawable.wood_background);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        Flow.FLUJOS.clear();
    }

}
