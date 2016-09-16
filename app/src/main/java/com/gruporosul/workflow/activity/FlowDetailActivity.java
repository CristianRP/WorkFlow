package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.adapter.HistoryAdapter;
import com.gruporosul.workflow.bean.FlowDetail;
import com.gruporosul.workflow.xml.ParserXmlFlowDetail;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class FlowDetailActivity extends AppCompatActivity
    implements HistoryAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private HistoryAdapter mAdaptador;
    private ProgressDialog mProgressDialog;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/WF_Flow_History?correlativo=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail);

        if (getIntent().getStringExtra("descripcion") != null && getIntent().getStringExtra("id") != null && getIntent().getStringExtra("agrupador") != null) {
            setToolbar(getIntent().getStringExtra("id") + "/" +
                    getIntent().getStringExtra("descripcion") + "/" +
                    getIntent().getStringExtra("agrupador"));
        } else {
            setToolbar("N/A");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerDetalle);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new HistoryAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(FlowDetailActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();


        new TareaDescargaXml().execute(URL + getIntent().getStringExtra("correlativo"));

        Log.e("Error en xml","");
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

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private class TareaDescargaXml extends AsyncTask<String, Void, List<FlowDetail>> {

        @Override
        protected List<FlowDetail> doInBackground(String... urls) {
            try {
                Log.e("url", urls[0]);
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
        protected void onPostExecute(List<FlowDetail> items) {

            FlowDetail.DETALLE = items;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        private List<FlowDetail> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlFlowDetail mParserXmlFlowDetail = new ParserXmlFlowDetail();
            List<FlowDetail> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlFlowDetail.parsear(mInputStream);

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
    public void onItemClick(HistoryAdapter.ViewHolder item, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        FlowDetail.DETALLE.clear();
    }

}
