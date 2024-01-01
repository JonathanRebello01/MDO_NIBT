package com.example.mdo_nibt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_ga_ministerio, edit_tel, edit_senha, edit_senha_confirmacao;
    private Button bt_cadastrar;
    private String usuarioID;
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
                    CadastrarUsuario(v);
                    Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void IniciarComponentes(){
        title_cadastro = findViewById(R.id.title_mdo);

        edit_nome = findViewById(R.id.edit_nome_cadastro);
        edit_email = findViewById(R.id.edit_email_cadastro);
        edit_ga_ministerio = findViewById(R.id.edit_ga_ministerio_cadastro);
        edit_tel = findViewById(R.id.edit_tel_cadstro);
        edit_senha = findViewById(R.id.edit_senha_cadastro);
        edit_senha_confirmacao = findViewById(R.id.edt_senha_confirmar_cadastro);

        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }

    private void CadastrarUsuario(View v){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    salvarDadosUsuario();
                }
                else {
                    String erro;
                    try {
                        throw Objects.requireNonNull(task.getException());
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Qual foi? A senha tá fraca! Coloca no mínimo 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Esse E-mail já tem no banco, só acessar tua conta, ou recuperar senha";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Isso nem é um E-mail";
                    }
                    catch (Exception e){
                        erro = "N sei qual foi o BO não";
                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
    }
    public void salvarDadosUsuario(){
        String nome = edit_nome.getText().toString();
        String ga_ministerio = edit_ga_ministerio.getText().toString();
        String tel = edit_tel.getText().toString();
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();


        FirebaseFirestore banco = FirebaseFirestore.getInstance();

        Map<String,String> usuarios = new HashMap<>();
        usuarios.put("Nome", nome);
        usuarios.put("Ga / Ministerio", ga_ministerio);
        usuarios.put("Telefone", tel);
        usuarios.put("E-mail", email);
        usuarios.put("Senha", senha);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = banco.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar os dados: " + e);
            }
        });

    }

}