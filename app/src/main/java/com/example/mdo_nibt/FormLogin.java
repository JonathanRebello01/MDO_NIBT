package com.example.mdo_nibt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FormLogin extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private Button bt_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        bt_login = findViewById(R.id.bt_entrar);
    }
    private void avancarPginaTeste(){

    }

}

