package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gruporosul.workflow.R;
import com.gruporosul.workflow.adapter.FlowAdapter;
import com.gruporosul.workflow.bean.Item;
import com.gruporosul.workflow.preferences.PrefManager;
import com.gruporosul.workflow.xml.ParserXmlItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FlowAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FlowAdapter mAdaptador;
    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    public static MainActivity mainActivity;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/WF_Type_List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        mPrefManager = new PrefManager(this);

        mainActivity = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerMain);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new FlowAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);

        mProgressDialog = new ProgressDialog(MainActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        new TareaDescargaXml().execute(URL);

        //throw new RuntimeException("I'm a cool exception and I crashed the main thread!");

    }

    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.txt_titulo));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Establecer icono del drawer toggle
            actionBar.setHomeAsUpIndicator(R.drawable.ic_flujo_de_trabajo);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mPrefManager.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TareaDescargaXml extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Item> items) {

            Item.ITEMS = items;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        private List<Item> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlItem mParserXmlItem = new ParserXmlItem();
            List<Item> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlItem.parsear(mInputStream);

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
    public void onItemClick(FlowAdapter.ViewHolder item, int position) {
        Item flow = Item.ITEMS.get(position);
        showMDialog(flow.getId() + "", flow.getTexto());

    }

    public void showMDialog(final String codFlujo, final String tipo) {

        new MaterialDialog.Builder(this)
                .title(R.string.txt_title_dialog)
                .items(R.array.estado)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        Intent buscar = new Intent(MainActivity.this, AgrupadorActivity.class);
                        buscar.putExtra("buscar", text);
                        buscar.putExtra("tipo", tipo);
                        buscar.putExtra("codFlujo", codFlujo);
                        startActivity(buscar);

                        return true;
                    }

                })
                //.icon(imagen())
                .positiveText(R.string.ok)
                .negativeText(R.string.cancelar)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        Item.ITEMS.clear();
    }

}
