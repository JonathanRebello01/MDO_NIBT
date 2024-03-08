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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FormLogin extends AppCompatActivity {
    private TextView nome_jonathan, nome_ana, nome_caio, text_tela_cadastro;
    private Button bt_login_jonathan, bt_login_ana, bt_login_caio;
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

        bt_login_jonathan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "at.dimmer@gmail.com";
                String senha = "123456";
                    autenticarUsuario(v, email, senha);
            }
        });

        bt_login_ana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "frotaana2005@gmail.com";
                String senha = "040520";
                autenticarUsuario(v, email, senha);
            }
        });

        bt_login_caio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "verascaio11@hotmail.com";
                String senha = "123456";
                autenticarUsuario(v, email, senha);
            }
        });

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

    }

    private void autenticarUsuario(View v, String email, String senha){

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

        bt_login_jonathan = findViewById(R.id.nome_jonathan);
        bt_login_ana = findViewById(R.id.nome_ana);
        bt_login_caio = findViewById(R.id.nome_caio);

        progressBar = findViewById(R.id.login_progressbar);

//        edt_email = findViewById(R.id.edit_email_login);
//        edt_senha = findViewById(R.id.edt_senha_login);

//        visualizar_senha = findViewById(R.id.visualizar_senha_login);
    }

}

