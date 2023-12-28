package com.example.mdo_nibt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_ga_ministerio, edit_tel, edit_senha, edit_senha_confirmacao;
    private Button bt_cadastrar;

    private TextView title_cadastro;
    String[] mensagens = {"Preencha todos os campos", "As senhas não conferem", "Cadastro realizado com sucesso"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        IniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String ga_ministerio = edit_ga_ministerio.getText().toString();
                String tel = edit_tel.getText().toString();
                String senha = edit_senha.getText().toString();
                String senha_confirmacao = edit_senha_confirmacao.getText().toString();

                if(nome.isEmpty() || email.isEmpty() || ga_ministerio.isEmpty() || tel.isEmpty() || senha.isEmpty() || senha_confirmacao.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else if (!senha.equals(senha_confirmacao)){
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    CadastrarUsuario();
                    Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
    private void IniciarComponentes(){
        title_cadastro = findViewById(R.id.title_mdo);

        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_ga_ministerio = findViewById(R.id.edit_ga_ministerio);
        edit_tel = findViewById(R.id.edit_tel);
        edit_senha = findViewById(R.id.edit_senha);
        edit_senha_confirmacao = findViewById(R.id.edit_senha_confirmar);

        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }

    private void CadastrarUsuario(){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
    }

}