package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.adapter.GrouperAdapter;
import com.gruporosul.workflow.bean.Agrupador;
import com.gruporosul.workflow.xml.ParserXmlGrouper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

public class AgrupadorActivity extends AppCompatActivity
        implements GrouperAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GrouperAdapter mAdaptador;
    private ProgressDialog mProgressDialog;

    public static AgrupadorActivity agrupadorActivity;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/WF_Flow_Grouper?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrupador);

        agrupadorActivity = this;

        Intent titulo = getIntent();
        if (titulo.getStringExtra("buscar") != null) {
            setToolbar("Agrupadores " + titulo.getStringExtra("buscar") + "s");
        } else {
            setToolbar("Nothing");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerAgrupador);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        mAdaptador = new GrouperAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setAdapter(new AlphaInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(AgrupadorActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        new TareaDescargaXml().execute(URL + "codFlujo=" + titulo.getStringExtra("codFlujo") + "&estado=" + titulo.getStringExtra("buscar"));

    }

    private class TareaDescargaXml extends AsyncTask<String, Void, List<Agrupador>> {

        @Override
        protected List<Agrupador> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Agrupador> agrupadors) {

            Agrupador.AGRUPADORES = agrupadors;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        private List<Agrupador> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlGrouper mParserXmlGrouper = new ParserXmlGrouper();
            List<Agrupador> agrupadors = null;

            try {

                mInputStream = descargarContenido(urlString);
                agrupadors = mParserXmlGrouper.parsear(mInputStream);

            } finally {
                if (mInputStream != null) {
                    mInputStream.close();
                }
            }

            return agrupadors;

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


    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar(String titulo) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(titulo);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Establecer icono del drawer toggle
            //actionBar.setHomeAsUpIndicator(R.drawable.logo_tr_rsz);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onItemClick(GrouperAdapter.ViewHolder item, int position) {
        Agrupador agrupador = Agrupador.AGRUPADORES.get(position);
        Intent newIntent = new Intent(AgrupadorActivity.this, FlujosActivity.class);
        newIntent.putExtra("agrupador", agrupador.getAgrupador());
        newIntent.putExtra("codFlujo", agrupador.getCodFlujo());
        newIntent.putExtra("tipo", getIntent().getStringExtra("tipo"));
        newIntent.putExtra("estado", getIntent().getStringExtra("buscar"));
        startActivity(newIntent);
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
        Agrupador.AGRUPADORES.clear();
    }

}
