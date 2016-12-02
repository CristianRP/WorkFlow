package com.gruporosul.workflow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Comodin;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComodinActivity extends AppCompatActivity {

    @BindString(R.string.title_activity_comodin)
    String titleActivity;
    @BindView(R.id.content_comodin)
    LinearLayout mContentComodin;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodin);

        ButterKnife.bind(this);

        setToolbar();

        mProgressDialog = new ProgressDialog(ComodinActivity.this);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        Log.e(":v", "creando inputs");
        for (int i = 0; i < Comodin.LISTA_COMODINES.size(); i ++) {
            TextInputLayout txtInput = createTextInputLayouts(Comodin.LISTA_COMODINES.get(i).getComodin());
            mContentComodin.addView(txtInput);
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

    private TextInputLayout createTextInputLayouts(String content) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.addView(createTextInputEditTexts(content));
        return textInputLayout;
    }

    private TextInputEditText createTextInputEditTexts(String content) {
        TextInputEditText textInputEditText = new TextInputEditText(this);
        textInputEditText.setHint(content);
        textInputEditText.setTag(content);
        return textInputEditText;
    }

}
