package com.example.mdo_nibt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MDO extends AppCompatActivity {

    private EditText edt_nome_mdo, edt_meditacao, edt_decoracao, edt_oracao;
    private Button btn_adicionar_mdo, btn_voltar;
    private ImageView ic_usuario, ic_meditacao, ic_decoracao, ic_oracao;
    private ProgressBar mdo_progressbar;
    private String usuarioID;
    private String ga_ministerio;
    private Boolean isGA = false;
    private FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private FirebaseFirestore banco_salvar = FirebaseFirestore.getInstance();
    private List<String> data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdo);
        getSupportActionBar().hide();
        iniciarComponentes();

        btn_adicionar_mdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edt_nome_mdo.getText().toString();
                String meditacao = edt_meditacao.getText().toString();
                String decoracao = edt_decoracao.getText().toString();
                String oracao = edt_oracao.getText().toString();

                salvaDataFirestore();

                mPessoaModel pessoa = new mPessoaModel(nome, meditacao, decoracao, oracao);
                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(isGA){
                    DocumentReference documentReference = banco_salvar.collection( "NIBT" + "/" + "GAs" + "/" + ga_ministerio + "/" + Util.dataAtual() + "/" + "MDOs").document(usuarioID);
                    // Adquira os dados atuais do documento
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Verifique se o documento já existe
                            if (documentSnapshot.exists()) {
                                // O documento já existe, então adicione a nova pessoa à lista existente
                                List<mPessoaModel> pessoas = (List<mPessoaModel>) documentSnapshot.get("pessoas");
                                if (pessoas == null) {
                                    pessoas = new ArrayList<>();
                                }
                                pessoas.add(pessoa);

                                // Atualize o documento com a lista atualizada
                                documentReference.update("pessoas", pessoas)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Dados atualizados com sucesso
                                                Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                                edt_nome_mdo.setText("");
                                                edt_meditacao.setText("");
                                                edt_decoracao.setText("");
                                                edt_oracao.setText("");
                                                carregamentoCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Trate falha ao atualizar dados
                                                Toast.makeText(getApplicationContext(), "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // O documento não existe, crie um novo documento com a lista contendo a primeira pessoa
                                List<mPessoaModel> pessoas = new ArrayList<>();
                                pessoas.add(pessoa);

                                // Crie o novo documento
                                documentReference.set(new HashMap<String, Object>() {{
                                            put("pessoas", pessoas);
                                        }})
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Dados adicionados com sucesso
                                                Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                                edt_nome_mdo.setText("");
                                                edt_meditacao.setText("");
                                                edt_decoracao.setText("");
                                                edt_oracao.setText("");
                                                carregamentoCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Trate falha ao adicionar dados
                                                Toast.makeText(getApplicationContext(), "Erro ao adicionar dados", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                }
                else {
                    DocumentReference documentReference = banco_salvar.collection( "NIBT" + "/" + "Ministerios" + "/" + ga_ministerio +  "/" + Util.dataAtual() + "/" + "MDOs" ).document(usuarioID);
                    // Adquira os dados atuais do documento
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Verifique se o documento já existe
                            if (documentSnapshot.exists()) {
                                // O documento já existe, então adicione a nova pessoa à lista existente
                                List<mPessoaModel> pessoas = (List<mPessoaModel>) documentSnapshot.get("pessoas");
                                if (pessoas == null) {
                                    pessoas = new ArrayList<>();
                                }
                                pessoas.add(pessoa);

                                // Atualize o documento com a lista atualizada
                                documentReference.update("pessoas", pessoas)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Dados atualizados com sucesso
                                                Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                                edt_nome_mdo.setText("");
                                                edt_meditacao.setText("");
                                                edt_decoracao.setText("");
                                                edt_oracao.setText("");
                                                carregamentoCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Trate falha ao atualizar dados
                                                Toast.makeText(getApplicationContext(), "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // O documento não existe, crie um novo documento com a lista contendo a primeira pessoa
                                List<mPessoaModel> pessoas = new ArrayList<>();
                                pessoas.add(pessoa);

                                // Crie o novo documento
                                documentReference.set(new HashMap<String, Object>() {{
                                            put("pessoas", pessoas);
                                        }})
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Dados adicionados com sucesso
                                                Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                                edt_nome_mdo.setText("");
                                                edt_meditacao.setText("");
                                                edt_decoracao.setText("");
                                                edt_oracao.setText("");
                                                carregamentoCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Trate falha ao adicionar dados
                                                Toast.makeText(getApplicationContext(), "Erro ao adicionar dados", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MDO.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarComponentes(){
        edt_nome_mdo = findViewById(R.id.edt_nome_mdo);
        edt_meditacao = findViewById(R.id.edt_meditacao);
        edt_decoracao = findViewById(R.id.edt_decoracao);
        edt_oracao = findViewById(R.id.edt_oracao);

        btn_adicionar_mdo = findViewById(R.id.btn_adicionar_mdo);
        btn_voltar = findViewById(R.id.bt_voltar_mdo);

        mdo_progressbar = findViewById(R.id.mdo_progressbar);

        ic_usuario = findViewById(R.id.iconeUser_mdo);
        ic_meditacao = findViewById(R.id.iconeMeditacao);
        ic_decoracao = findViewById(R.id.iconeDecoracao);
        ic_oracao = findViewById(R.id.iconeOracao);
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = banco_recuperar.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    if(documentSnapshot.getString("Lider") != null){
                        ga_ministerio = documentSnapshot.getString("Lider");
                        isGA = true;
                    }
                    else {
                        ga_ministerio = documentSnapshot.getString("Ministerio");
                    }
                }
            }
        });
    }

    private void salvaDataFirestore() {
            DocumentReference documentReferenceSalvarData = banco_salvar.collection("NIBT" + "/" + "historicoUsuarios" + "/" + "datas").document(usuarioID);
            documentReferenceSalvarData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // Verifique se o documento já existe
                    if (documentSnapshot.exists()) {
                        // O documento já existe, então adicione a nova pessoa à lista existente
                        List<String> data = (List<String>) documentSnapshot.get("data");

                        assert data != null;
                        if(!data.contains(Util.dataAtual())){
                            data.add(Util.dataAtual());
                        }


                        // Atualize o documento com a lista atualizada
                        documentReferenceSalvarData.update("data", data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Dados atualizados com sucesso
                                        Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Trate falha ao atualizar dados
                                        Toast.makeText(getApplicationContext(), "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        // O documento não existe, crie um novo documento com a lista contendo a primeira pessoa
                        List<String> data = new ArrayList<>();
                        data.add(Util.dataAtual());

                        // Crie o novo documento
                        documentReferenceSalvarData.set(new HashMap<String, Object>() {{
                                    put("data", data);
                                }})
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Dados adicionados com sucesso
                                        Toast.makeText(getApplicationContext(), "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Trate falha ao adicionar dados
                                        Toast.makeText(getApplicationContext(), "Erro ao adicionar dados", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
    }

    private void carregamentoCampos(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edt_nome_mdo.setVisibility(View.INVISIBLE);
                edt_meditacao.setVisibility(View.INVISIBLE);
                edt_decoracao.setVisibility(View.INVISIBLE);
                edt_oracao.setVisibility(View.INVISIBLE);
                ic_usuario.setVisibility(View.INVISIBLE);
                ic_meditacao.setVisibility(View.INVISIBLE);
                ic_decoracao.setVisibility(View.INVISIBLE);
                ic_oracao.setVisibility(View.INVISIBLE);
                mdo_progressbar.setVisibility(View.VISIBLE);
            }
        }, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edt_nome_mdo.setVisibility(View.VISIBLE);
                edt_meditacao.setVisibility(View.VISIBLE);
                edt_decoracao.setVisibility(View.VISIBLE);
                edt_oracao.setVisibility(View.VISIBLE);
                ic_usuario.setVisibility(View.VISIBLE);
                ic_meditacao.setVisibility(View.VISIBLE);
                ic_decoracao.setVisibility(View.VISIBLE);
                ic_oracao.setVisibility(View.VISIBLE);
                mdo_progressbar.setVisibility(View.INVISIBLE);
            }
        }, 1500);
    }
}
