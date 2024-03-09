package com.example.mdo_nibt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario, txt_primeiro, txt_segundo, txt_terceiro;
    private Button bt_deslogar, bt_cadastrarMDO, bt_historico;
    private ImageView bt_ferramentas;
    FirebaseFirestore banco = FirebaseFirestore.getInstance();
    FirebaseFirestore outro_banco = FirebaseFirestore.getInstance();
    String usuarioID, ga_ministerio;
    String pts_dimmer, pts_canon, pts_delay;
    int pontos_dimmer;
    int pontos_canon;
    int pontos_delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getSupportActionBar().hide();
        iniciarComponentes();
        bt_cadastrarMDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TelaPrincipal.this, MDO.class);
                startActivity(intent);

            }
        });

        bt_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TelaPrincipal.this, Historico.class);
                startActivity(intent);

            }
        });

        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        TelaPrincipal.this).create();
                alertDialog.setTitle("Deslogar usuário");
                alertDialog
                        .setMessage("Você tem certeza que deseja desconectar?");
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
                                startActivity(intent);
                            }

                        });
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.show();
            }
        });
        bt_ferramentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, ContadorActivity.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarComponentes(){
        nomeUsuario = findViewById(R.id.text_nome_usuario_telaprincipal);
        emailUsuario = findViewById(R.id.text_email_usuario_telaprincipal);

        bt_deslogar = findViewById(R.id.btn_deslogar_telaprincipal);
        bt_cadastrarMDO = findViewById(R.id.btn_avancar_mdo_telaprincipal);
        bt_historico = findViewById(R.id.btn_ver_historico_telaprincipal);

        bt_ferramentas = findViewById(R.id.ic_tools);

        txt_primeiro = findViewById(R.id.text_primeiro);
        txt_segundo = findViewById(R.id.text_segundo);
        txt_terceiro = findViewById(R.id.text_terceiro);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = banco.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nomeUsuario.setText(documentSnapshot.getString("Nome"));
                    emailUsuario.setText(email);
                }
            }
        });

        DocumentReference documentReference2 = banco.collection("Usuarios").document(usuarioID);
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    if(documentSnapshot.getString("Lider") != null){
                        ga_ministerio = documentSnapshot.getString("Lider");
                    }
                    else {
                        ga_ministerio = documentSnapshot.getString("Ministerio");
                    }
                }
                DocumentReference get_pts = outro_banco.collection( "NIBT" + "/" + "DINAMICAS" + "/" + ga_ministerio + "/").document("pontuacaoTotal");
                get_pts.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            System.out.println("teste "+ value);
                            Map getData = new HashMap<String,Object>();
                            getData = value.getData();

                            Map<String, String> pontuacaoMap = (Map<String, String>) getData.get("pontuacao");

                            if(value.getData() != null){
                                pts_dimmer = pontuacaoMap.get("Dimmer");
                            }
                            if(value.getData() != null){
                                pts_canon = pontuacaoMap.get("Canon");
                            }
                            if(value.getData() != null){
                                pts_delay = pontuacaoMap.get("Delay");
                            }
                            if (pts_dimmer != null){
                                pontos_dimmer = Integer.parseInt(pts_dimmer);
                            }
                            if (pts_canon != null){
                                pontos_canon = Integer.parseInt(pts_canon);
                            }
                            if (pts_delay != null){
                                pontos_delay = Integer.parseInt(pts_delay);
                            }
                            ordenarRanking();
                        }
                    }
                });
            }
        });

    }

    private void ordenarRanking(){
        if (pontos_dimmer >= pontos_canon && pontos_dimmer >= pontos_delay) {
            if (pontos_canon >= pontos_delay) {
                txt_primeiro.setText("1° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");

            } else {
                txt_primeiro.setText("1° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
            }
        } else if (pontos_canon >= pontos_dimmer && pontos_canon >= pontos_delay) {
            if (pontos_dimmer >= pontos_delay) {
                txt_primeiro.setText("1° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");
            } else {
                txt_primeiro.setText("1° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
            }
        } else {
            if (pontos_dimmer >= pontos_canon) {
                txt_primeiro.setText("1° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
            } else {
                txt_primeiro.setText("1° Lugar: " + "Delay" + " => " + pontos_delay + " Pontos!");
                txt_segundo.setText("2° Lugar: " + "Canon" + " => " + pontos_canon + " Pontos!");
                txt_terceiro.setText("3° Lugar: " + "Dimmer" + " => " + pontos_dimmer + " Pontos!");
            }
        }
    }
}