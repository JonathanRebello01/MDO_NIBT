package com.example.mdo_nibt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private Button bt_login;
    private EditText edt_email, edt_senha;
    private ProgressBar progressBar;
    private ImageView visualizar_senha;
    boolean password_invisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes();

        visualizar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password_invisible){
                    edt_senha.setInputType(InputType.TYPE_CLASS_NUMBER);
                    password_invisible = false;
                }

                else {
                    edt_senha.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    password_invisible = true;
                }
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String senha = edt_senha.getText().toString();
                if(email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, "Preencha TODOS os campos ; )", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    autenticarUsuario(v);
                }
            }
        });

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });
    }

    private void autenticarUsuario(View v){

        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincipal();
                        }
                    },0);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(v, "Erro na tentativa de login" + e, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            telaPrincipal();
        }
    }

    private void telaPrincipal(){

        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);

    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);

        bt_login = findViewById(R.id.bt_entrar);


        progressBar = findViewById(R.id.login_progressbar);

        edt_email = findViewById(R.id.edit_email_login);
        edt_senha = findViewById(R.id.edt_senha_login);

        visualizar_senha = findViewById(R.id.visualizar_senha_login);
    }

}

