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
import java.util.Map;

public class ContadorActivity extends AppCompatActivity {

    private EditText contador_dimmer, contador_canon, contador_delay;
    private ImageView somar_dimmer, diminuir_dimmer, somar_canon, diminur_canon, somar_delay, diminuir_delay;
    private ProgressBar pg;
    private Button salvar_vencedor, voltar;
    private int cont_dimmer, cont_canon, cont_delay;
    private FirebaseFirestore banco_salvar = FirebaseFirestore.getInstance();
    private FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private String usuarioID;
    private String ga_ministerio, salvar_dimmer_total, salvar_canon_total, salvar_delay_total;
    private boolean dimmerVenceu = false;
    private boolean canonVenceu = false;
    private boolean delayVenceu= false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);
        getSupportActionBar().hide();
        IniciarComponentes();

        somar_dimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_dimmer.setText(String.valueOf(cont_dimmer+1));
                cont_dimmer += 1;
            }
        });
        diminuir_dimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_dimmer.setText(String.valueOf(cont_dimmer-1));
                cont_dimmer-=1;
            }
        });
        somar_canon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_canon.setText(String.valueOf(cont_canon+1));
                cont_canon+=1;
            }
        });
        diminur_canon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_canon.setText(String.valueOf(cont_canon-1));
                cont_canon-=1;
            }
        });
        somar_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_delay.setText(String.valueOf(cont_delay+1));
                cont_delay+=1;
            }
        });
        diminuir_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador_delay.setText(String.valueOf(cont_delay-1));
                cont_delay-=1;
            }
        });

        salvar_vencedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaVencedores();
                carregamentoCampos();
                salvarPontuacaoParcial();
                salvarPontuacaoTotal(dimmerVenceu, canonVenceu, delayVenceu);
                dimmerVenceu = false;
                canonVenceu = false;
                delayVenceu = false;
                cont_dimmer = 0;
                cont_canon = 0;
                cont_delay = 0;
                contador_dimmer.setText("");
                contador_canon.setText("");
                contador_delay.setText("");
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContadorActivity.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });
    }

    private void verificaVencedores() {
        if (cont_dimmer > cont_canon && cont_dimmer > cont_delay) {
            dimmerVenceu = true;
        } else if (cont_delay > cont_canon && cont_delay > cont_dimmer) {
            delayVenceu = true;
        } else if (cont_canon > cont_dimmer && cont_canon > cont_delay) {
            canonVenceu = true;
        } else if (cont_dimmer == cont_canon && cont_dimmer == cont_delay) {
            dimmerVenceu = true;
            canonVenceu = true;
            delayVenceu = true;
        } else if (cont_dimmer == cont_canon) {
            dimmerVenceu = true;
            canonVenceu = true;
        } else if (cont_dimmer == cont_delay) {
            dimmerVenceu = true;
            delayVenceu = true;
        } else if (cont_canon == cont_delay) {
            canonVenceu = true;
            delayVenceu = true;
        }
    }

    private void IniciarComponentes() {
        contador_dimmer = findViewById(R.id.contador_dimmer);
        contador_canon = findViewById(R.id.contador_canon);
        contador_delay = findViewById(R.id.contador_delay);

        somar_dimmer = findViewById(R.id.bt_somar_dimmer);
        diminuir_dimmer = findViewById(R.id.bt_diminuir_dimmer);
        somar_canon = findViewById(R.id.bt_somar_canon);
        diminur_canon = findViewById(R.id.bt_diminuir_canon);
        somar_delay = findViewById(R.id.bt_somar_delay);
        diminuir_delay = findViewById(R.id.bt_diminuir_delay);

        pg = findViewById(R.id.dinamica_progressbar);

        salvar_vencedor = findViewById(R.id.btn_salvar_vencedor);
        voltar = findViewById(R.id.bt_voltar_contador);

        cont_dimmer = 0;
        cont_canon = 0;
        cont_delay = 0;
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
                    }
                    else {
                        ga_ministerio = documentSnapshot.getString("Ministerio");
                    }
                }
            }
        });
    }
    private void carregamentoCampos(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contador_dimmer.setVisibility(View.INVISIBLE);
                somar_dimmer.setVisibility(View.INVISIBLE);
                diminuir_dimmer.setVisibility(View.INVISIBLE);
                contador_canon.setVisibility(View.INVISIBLE);
                somar_canon.setVisibility(View.INVISIBLE);
                diminur_canon.setVisibility(View.INVISIBLE);
                contador_delay.setVisibility(View.INVISIBLE);
                somar_delay.setVisibility(View.INVISIBLE);
                diminuir_delay.setVisibility(View.INVISIBLE);
                voltar.setVisibility(View.INVISIBLE);
                pg.setVisibility(View.VISIBLE);

            }
        }, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contador_dimmer.setVisibility(View.VISIBLE);
                somar_dimmer.setVisibility(View.VISIBLE);
                diminuir_dimmer.setVisibility(View.VISIBLE);
                contador_canon.setVisibility(View.VISIBLE);
                somar_canon.setVisibility(View.VISIBLE);
                diminur_canon.setVisibility(View.VISIBLE);
                contador_delay.setVisibility(View.VISIBLE);
                somar_delay.setVisibility(View.VISIBLE);
                diminuir_delay.setVisibility(View.VISIBLE);
                voltar.setVisibility(View.VISIBLE);
                pg.setVisibility(View.INVISIBLE);
            }
        }, 1500);
    }
    private void salvarPontuacaoParcial(){
        String salvar_dimmer_parcial = contador_dimmer.getText().toString();
        String salvar_canon_parcial = contador_canon.getText().toString();
        String salvar_delay_parcial = contador_delay.getText().toString();


        mPontuacaoParcialModel pontuacao = new mPontuacaoParcialModel(salvar_dimmer_parcial, salvar_canon_parcial, salvar_delay_parcial);

        DocumentReference documentReference = banco_salvar.collection( "NIBT" + "/" + "DINAMICAS" + "/" + ga_ministerio + "/").document("pontuacaoParcial");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Verifique se o documento já existe
                if (documentSnapshot.exists()) {
                    // Atualize o documento com a lista atualizada
                    List<mPontuacaoParcialModel> pontuacoes = (List<mPontuacaoParcialModel>) documentSnapshot.get("pontuacao");
                    if (pontuacoes == null) {
                        pontuacoes = new ArrayList<>();
                    }
                    pontuacoes.add(pontuacao);

                    // Atualize o documento com a lista atualizada
                    documentReference.update("pontuacao", pontuacoes)
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
                } else {
                    // Crie o novo documento
                    List<mPontuacaoParcialModel> pontuacoes = new ArrayList<>();
                    pontuacoes.add(pontuacao);

                    // Crie o novo documento
                    documentReference.set(new HashMap<String, Object>() {{
                                put("pontuacao", pontuacoes);
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
    private void salvarPontuacaoTotal(boolean dimmerVenceu, boolean canonVenceu, boolean delayVenceu){

        DocumentReference documentReference = banco_salvar.collection( "NIBT" + "/" + "DINAMICAS" + "/" + ga_ministerio + "/").document("pontuacaoTotal");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Verifique se o documento já existe
                if (documentSnapshot.exists()) {

                    Map pontuada = (Map) documentSnapshot.get("pontuacao");
                    if (pontuada == null){
                        pontuada = new HashMap<String,String>();
                    }


                    if(pontuada.get("Dimmer") == null){
                        salvar_dimmer_total = "0";
                        if (dimmerVenceu){
                            salvar_dimmer_total = String.valueOf(Integer.parseInt(salvar_dimmer_total) + 1);
                            pontuada.put("Dimmer", salvar_dimmer_total);
                        }
                    }
                    else {
                        if (dimmerVenceu) {
                            salvar_dimmer_total = String.valueOf(Integer.parseInt((String) pontuada.get("Dimmer")) + 1);
                            pontuada.put("Dimmer", salvar_dimmer_total);
                        }
                    }
                    if(pontuada.get("Canon") == null){
                        salvar_canon_total ="0";
                        if(canonVenceu){
                            salvar_canon_total = String.valueOf(Integer.parseInt(salvar_canon_total) + 1);
                            pontuada.put("Canon", salvar_canon_total);
                        }
                    }
                    else {
                        if(canonVenceu) {
                            salvar_canon_total = String.valueOf(Integer.parseInt((String) pontuada.get("Canon")) + 1);
                            pontuada.put("Canon", salvar_canon_total);
                        }
                    }
                    if (pontuada.get("Delay") == null){
                        salvar_delay_total = "0";
                        if (delayVenceu){
                            salvar_delay_total = String.valueOf(Integer.parseInt(salvar_delay_total) + 1);
                            pontuada.put("Delay", salvar_delay_total);
                        }
                    }
                    else {
                        if (delayVenceu) {
                            salvar_delay_total = String.valueOf(Integer.parseInt((String) pontuada.get("Delay")) + 1);
                            pontuada.put("Delay", salvar_delay_total);
                        }
                    }

                    // Atualize o documento com a lista atualizada
                    documentReference.update("pontuacao", pontuada)
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
                } else {
                    Map pontuada = new HashMap<String,String>();

                    if(pontuada.get("Dimmer") == null){
                        salvar_dimmer_total = "0";
                        if (dimmerVenceu){
                            salvar_dimmer_total = String.valueOf(Integer.parseInt(salvar_dimmer_total) + 1);
                            pontuada.put("Dimmer", salvar_dimmer_total);
                        }
                    }
                    else {
                        if (dimmerVenceu) {
                            salvar_dimmer_total = String.valueOf(Integer.parseInt((String) pontuada.get("Dimmer")) + 1);
                            pontuada.put("Dimmer", salvar_dimmer_total);
                        }
                    }
                    if(pontuada.get("Canon") == null){
                        salvar_canon_total ="0";
                        if(canonVenceu){
                            salvar_canon_total = String.valueOf(Integer.parseInt(salvar_canon_total) + 1);
                            pontuada.put("Canon", salvar_canon_total);
                        }
                    }
                    else {
                        if(canonVenceu) {
                            salvar_canon_total = String.valueOf(Integer.parseInt((String) pontuada.get("Canon")) + 1);
                            pontuada.put("Canon", salvar_canon_total);
                        }
                    }
                    if (pontuada.get("Delay") == null){
                        salvar_delay_total = "0";
                        if (delayVenceu){
                            salvar_delay_total = String.valueOf(Integer.parseInt(salvar_delay_total) + 1);
                            pontuada.put("Delay", salvar_delay_total);
                        }
                    }
                    else {
                        if (delayVenceu) {
                            salvar_delay_total = String.valueOf(Integer.parseInt((String) pontuada.get("Delay")) + 1);
                            pontuada.put("Delay", salvar_delay_total);
                        }
                    }

                    documentReference.set(pontuada)
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

}
