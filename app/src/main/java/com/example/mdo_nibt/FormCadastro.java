package com.example.mdo_nibt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_tel, edit_senha, edit_senha_confirmacao, edt_nome_lider_ga;
    private AutoCompleteTextView complete_ga_ministerios;
    private TextInputLayout caixa_complete_ga_ministerios;
    private Button bt_cadastrar;
    private String usuarioID;
    private TextView title_cadastro;
    private Context ctx;
    private ArrayList<String> MINISTERIOS = new ArrayList<>();
    private SwitchCompat sw_lider_ga;
    private boolean lider_ga = false;
    private ArrayAdapter<String> adapterMinisterios;
    private Map<String,String> usuarios = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        ctx = getBaseContext();

        getSupportActionBar().hide();
        IniciarComponentes();


        sw_lider_ga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!lider_ga){

                    complete_ga_ministerios.setVisibility(View.INVISIBLE);
                    caixa_complete_ga_ministerios.setVisibility(View.INVISIBLE);

                    edt_nome_lider_ga.setVisibility(View.VISIBLE);

                    lider_ga = true;
                }
                else {

                    edt_nome_lider_ga.setVisibility(View.INVISIBLE);

                    complete_ga_ministerios.setVisibility(View.VISIBLE);
                    caixa_complete_ga_ministerios.setVisibility(View.VISIBLE);

                    lider_ga = false;
                }
            }
        });
        complete_ga_ministerios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete_ga_ministerios.showDropDown();

            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String ministerio = complete_ga_ministerios.getText().toString();
                String nome_lider_ga = edt_nome_lider_ga.getText().toString();
                String tel = edit_tel.getText().toString();
                String senha = edit_senha.getText().toString();
                String senha_confirmacao = edit_senha_confirmacao.getText().toString();




                if (lider_ga) {
                    if (nome.isEmpty() || email.isEmpty() || nome_lider_ga.isEmpty() || tel.isEmpty() || senha.isEmpty() || senha_confirmacao.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Preencha TODOS os campos ; )", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else if (!senha.equals(senha_confirmacao)) {
                        Snackbar snackbar = Snackbar.make(v, "As senhas não conferem", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        CadastrarUsuario(v);
                        FirebaseAuth.getInstance().signOut();
                    }
                }
                else {
                    if (nome.isEmpty() || email.isEmpty() || ministerio.isEmpty() || tel.isEmpty() || senha.isEmpty() || senha_confirmacao.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Preencha TODOS os campos ; )", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else if (!senha.equals(senha_confirmacao)) {
                        Snackbar snackbar = Snackbar.make(v, "As senhas não conferem", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        CadastrarUsuario(v);
                    }
                }
            }
        });
    }
    private void IniciarComponentes(){
        title_cadastro = findViewById(R.id.title_mdo);

        edit_nome = findViewById(R.id.edit_nome_cadastro);
        edit_email = findViewById(R.id.edit_email_cadastro);
        edit_tel = findViewById(R.id.edit_tel_cadstro);
        edit_senha = findViewById(R.id.edit_senha_cadastro);
        edit_senha_confirmacao = findViewById(R.id.edt_senha_confirmar_cadastro);
        edt_nome_lider_ga = findViewById(R.id.edit_nome_lider_ga_cadstro);

        bt_cadastrar = findViewById(R.id.bt_cadastrar);

        sw_lider_ga = findViewById(R.id.sw_lider_ga);


        adapterMinisterios = new ArrayAdapter<>(ctx, android.R.layout.simple_selectable_list_item, MINISTERIOS);
        MINISTERIOS.add("Apoio Técnico");
        MINISTERIOS.add("Dança");
        MINISTERIOS.add("DNA");
        MINISTERIOS.add("NovaTeste");

        caixa_complete_ga_ministerios = findViewById(R.id.edit_ministerio_cadastro);
        complete_ga_ministerios = findViewById(R.id.expor_edit_ministerio_cadastro);
        complete_ga_ministerios.setAdapter(adapterMinisterios);

    }

    private void CadastrarUsuario(View v){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast toast = Toast.makeText(ctx, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT);
                    toast.show();

                    salvarDadosUsuario();

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                    startActivity(intent);
                }
                else {
                    String erro;
                    try {
                        throw Objects.requireNonNull(task.getException());
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Senha fraca; Forneça uma senha de, no mínimo 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "E-mail já cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }
                    catch (Exception e){
                        erro = "Houve um erro inesperado";
                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
    }
    public void salvarDadosUsuario(){
        String nome = edit_nome.getText().toString();
        String ministerio = complete_ga_ministerios.getText().toString();
        String nome_lider_ga = edt_nome_lider_ga.getText().toString();
        String tel = edit_tel.getText().toString();
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();


        System.out.println("teste" + lider_ga);

        FirebaseFirestore banco = FirebaseFirestore.getInstance();

        if (!lider_ga){
            Map<String,String> usuarios = new HashMap<>();
            usuarios.put("Nome", nome);
            usuarios.put("Ministerio", ministerio);
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
        else {
            Map<String,String> usuarios = new HashMap<>();
            usuarios.put("Nome", nome);
            usuarios.put("Líder", nome_lider_ga);
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

}