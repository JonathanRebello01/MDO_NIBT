package com.example.mdo_nibt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button bt_login_vareta, bt_login_bastao, bt_login_barbante;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes();

        bt_login_vareta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "@gmail.com";
                String senha = "123456";
                    autenticarUsuario(v, email, senha);
            }
        });

        bt_login_bastao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "@gmail.com";
                String senha = "040520";
                autenticarUsuario(v, email, senha);
            }
        });

        bt_login_barbante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "@hotmail.com";
                String senha = "123456";
                autenticarUsuario(v, email, senha);
            }
        });

    }

    private void autenticarUsuario(View v, String email, String senha){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
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

        bt_login_vareta = findViewById(R.id.nome_vareta);
        bt_login_bastao = findViewById(R.id.nome_bastao);
        bt_login_barbante = findViewById(R.id.nome_barbante);

    }

}

