package com.example.mdo_nibt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class VisualizacaoMDO extends AppCompatActivity {

    private TextView txt_titulo;
    private String data, dataFormatada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizacao_mdo);
        getSupportActionBar().hide();
        iniciarComponentes();

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("data");
            dataFormatada = Util.formatarData(data);
            txt_titulo.setText(dataFormatada);
        }
    }
    private void iniciarComponentes(){
        txt_titulo = findViewById(R.id.title_visualizacao_mdo);
    }
}