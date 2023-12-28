package com.example.mdo_nibt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FormLogin extends AppCompatActivity {
    private TextView text_tela_cadastro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(FormLogin.this, com.example.mdo_nibt.FormCadastro.class);
                startActivity(intent);
            }
        });

    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }

}

