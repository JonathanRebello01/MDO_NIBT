package com.example.mdo_nibt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario, txt_primeiro, txt_segundo, txt_terceiro;
    private Button bt_deslogar, bt_cadastrarMDO, bt_historico, btnLimparNotas, btnSalvarNotas;
    private ImageView bt_ferramentas;

    // 🔵 Bloco de notas (simples)
    private EditText anotacoes;
    private Button btn_salvar_notas;

    private final FirebaseFirestore banco = FirebaseFirestore.getInstance();
    private String usuarioID, ga_ministerio;
    private String pts_dimmer, pts_canon, pts_delay;
    private int pontos_dimmer;
    private int pontos_canon;
    private int pontos_delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        iniciarComponentes();

        bt_cadastrarMDO.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, MDO.class);
            startActivity(intent);
        });

        bt_historico.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, Historico.class);
            startActivity(intent);
        });

        btnSalvarNotas.setOnClickListener(v -> salvarNota());

        btnLimparNotas.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this) // Activity; em Fragment use requireContext()
                    .setTitle("Limpar anotações")
                    .setMessage("Tem certeza que deseja apagar o conteúdo das anotações?")
                    .setPositiveButton("Apagar", (dialog, which) -> {
                        anotacoes.setText("");
                        salvarNota(); // persiste em branco
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        });

        bt_deslogar.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(TelaPrincipal.this).create();
            alertDialog.setTitle("Deslogar usuário");
            alertDialog.setMessage("Você tem certeza que deseja desconectar?");
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                    (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(TelaPrincipal.this, FormLoginSimplificadoLog.class);
                        startActivity(intent);
                        finish(); // ✅ encerra a Activity atual
                    });
            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar",
                    (dialog, which) -> {});
            alertDialog.show();
        });

//        bt_ferramentas.setOnClickListener(v -> {
//            Intent intent = new Intent(TelaPrincipal.this, ContadorActivity.class);
//            startActivity(intent);
//        });
    }

    private void iniciarComponentes() {
        nomeUsuario = findViewById(R.id.text_nome_usuario_telaprincipal);
        emailUsuario = findViewById(R.id.text_email_usuario_telaprincipal);

        bt_deslogar = findViewById(R.id.btn_deslogar_telaprincipal);
        bt_cadastrarMDO = findViewById(R.id.btn_avancar_mdo_telaprincipal);
        bt_historico = findViewById(R.id.btn_ver_historico_telaprincipal);

        // 🔵 IDs do bloco de notas (precisam existir no XML)
        anotacoes = findViewById(R.id.anotacoes);
        btn_salvar_notas = findViewById(R.id.btn_salvar_notas);
        btnSalvarNotas = findViewById(R.id.btn_salvar_notas);
        btnLimparNotas = findViewById(R.id.btn_limpar_notas);

//        bt_ferramentas = findViewById(R.id.ic_tools);
//        txt_primeiro = findViewById(R.id.text_primeiro);
//        txt_segundo = findViewById(R.id.text_segundo);
//        txt_terceiro = findViewById(R.id.text_terceiro);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // ✅ segurança: se não estiver logado, volta pro login
            startActivity(new Intent(this, FormLoginSimplificadoLog.class));
            finish();
            return;
        }

        String email = user.getEmail();
        usuarioID = user.getUid();

        // 🔵 Carregar anotação única do Firestore (listener em tempo real)
        DocumentReference refNota = banco
                .collection("Usuarios")
                .document(usuarioID)
                .collection("Notas")
                .document("AnotacaoUnica");

        refNota.addSnapshotListener((snapshot, error) -> {
            if (error != null) return;
            if (snapshot != null && snapshot.exists()) {
                String texto = snapshot.getString("texto");
                if (texto != null && !texto.equals(anotacoes.getText().toString())) {
                    anotacoes.setText(texto);
                }
            }
        });

        // 🔵 Seus listeners já existentes
        DocumentReference documentReference = banco.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) return;
                if (documentSnapshot != null) {
                    nomeUsuario.setText(documentSnapshot.getString("Nome"));
                    emailUsuario.setText(email);
                }
            }
        });

        DocumentReference documentReference2 = banco.collection("Usuarios").document(usuarioID);
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) return;
                if (documentSnapshot != null) {
                    if (documentSnapshot.getString("Lider") != null) {
                        ga_ministerio = documentSnapshot.getString("Lider");
                    } else {
                        ga_ministerio = documentSnapshot.getString("Ministerio");
                    }
                }
                // Seu código do ranking está comentado no original; mantendo assim
            }
        });
    }

    // 🔵 Salva o conteúdo do EditText em um único documento no Firestore
    private void salvarNota() {
        if (usuarioID == null) return;

        String texto = anotacoes.getText().toString();

        Map<String, Object> dado = new HashMap<>();
        dado.put("texto", texto);

        banco.collection("Usuarios")
                .document(usuarioID)
                .collection("Notas")
                .document("AnotacaoUnica")
                .set(dado)
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Anotação salva!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Falha ao salvar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void ordenarRanking() {
        if (pontos_dimmer >= pontos_canon && pontos_dimmer >= pontos_delay) {
            if (pontos_canon >= pontos_delay) {
                txt_primeiro.setText("1° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
                txt_segundo.setText("2° Lugar: Canon  => " + pontos_canon + " Pontos!");
                txt_terceiro.setText("3° Lugar: Delay  => " + pontos_delay + " Pontos!");
            } else {
                txt_primeiro.setText("1° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
                txt_segundo.setText("2° Lugar: Delay  => " + pontos_delay + " Pontos!");
                txt_terceiro.setText("3° Lugar: Canon  => " + pontos_canon + " Pontos!");
            }
        } else if (pontos_canon >= pontos_dimmer && pontos_canon >= pontos_delay) {
            if (pontos_dimmer >= pontos_delay) {
                txt_primeiro.setText("1° Lugar: Canon  => " + pontos_canon + " Pontos!");
                txt_segundo.setText("2° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
                txt_terceiro.setText("3° Lugar: Delay  => " + pontos_delay + " Pontos!");
            } else {
                txt_primeiro.setText("1° Lugar: Canon  => " + pontos_canon + " Pontos!");
                txt_segundo.setText("2° Lugar: Delay  => " + pontos_delay + " Pontos!");
                txt_terceiro.setText("3° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
            }
        } else {
            if (pontos_dimmer >= pontos_canon) {
                txt_primeiro.setText("1° Lugar: Delay  => " + pontos_delay + " Pontos!");
                txt_segundo.setText("2° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
                txt_terceiro.setText("3° Lugar: Canon  => " + pontos_canon + " Pontos!");
            } else {
                txt_primeiro.setText("1° Lugar: Delay  => " + pontos_delay + " Pontos!");
                txt_segundo.setText("2° Lugar: Canon  => " + pontos_canon + " Pontos!");
                txt_terceiro.setText("3° Lugar: Dimmer => " + pontos_dimmer + " Pontos!");
            }
        }
    }
}