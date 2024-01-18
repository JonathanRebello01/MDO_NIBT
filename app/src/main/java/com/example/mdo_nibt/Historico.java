package com.example.mdo_nibt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class Historico extends AppCompatActivity {
    private RecyclerView rv_historico;
    private String usuarioID, ga_ministerio;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private Boolean isGA = false;
    private String listaDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        getSupportActionBar().hide();
        iniciarComponentes();

        ArrayList<String> lista = new ArrayList<>();
        lista.add("14/04/2004");
        lista.add("20/01/2001");
        lista.add("02/12/2005");

        HistoricoAdapter historicoAdapter = new HistoricoAdapter(lista);
        rv_historico.setAdapter(historicoAdapter);
        rv_historico.setLayoutManager(new LinearLayoutManager(this));

    }

    private void iniciarComponentes(){
        rv_historico = findViewById(R.id.rv_historico);
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = banco_recuperar.collection("Usuarios").document(usuarioID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.getString("Lider") != null) {
                        ga_ministerio = documentSnapshot.getString("Lider");
                        isGA = true;
                    } else {
                        ga_ministerio = documentSnapshot.getString("Ministerio");
                    }
                }

//                if (isGA) {
//                    DocumentReference documentReference1 = banco_recuperar.document("NIBT" + "/" + "MDOs" + "/" + "GAs" + "/" + ga_ministerio);
//
//                    banco_recuperar.collection("NIBT").document("MDOs").collection("GAs").document(ga_ministerio).get()
//                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()){
//
//                                        DocumentSnapshot collectionReference = task.getResult();
//
//                                        if (collectionReference != null) {
//                                            for (DocumentReference subcollectionRef : collectionReference.getDocuments()) {
//                                                // Obtendo o nome de cada subcoleção
//                                                String nomeSubcolecao = subcollectionRef.getId();
//                                                Log.d("TAG", "Nome da subcoleção: " + nomeSubcolecao);
//                                            }
//
//                                    }
//                                }
//                            });

//                    documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                            if (value != null) {
//
//
//
//                                System.out.println("teste: " + value);
//
//                            }
//                        }
//                    });
//                } else {
//                    DocumentReference documentReference1 = banco_recuperar.document("NIBT" + "/" + "Ministerios" + "/" + "GAs" + "/" + ga_ministerio);
//                    documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                            if (value != null) {
//                                listaDatas =value.toString();
//
//
//                                System.out.println("teste: " + value);
//
//                            }
//                        }
//                    });
//                }
            }
        });
    }
}