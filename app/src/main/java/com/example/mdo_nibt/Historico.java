package com.example.mdo_nibt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Historico extends AppCompatActivity {
    private RecyclerView rv_historico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        getSupportActionBar().hide();
        iniciarComponentes();

        ArrayList <String> lista = new ArrayList<>();
        lista.add("14/04/2004");
        lista.add("20/01/2001");
        lista.add("02/12/2005");

        HistoricoAdapter historicoAdapter = new HistoricoAdapter(lista);
        rv_historico.setAdapter(historicoAdapter);
        rv_historico.setLayoutManager(new LinearLayoutManager(this));

    }

    private void iniciarComponentes(){
        rv_historico = findViewById(R.id.rv_historico);
    }
}