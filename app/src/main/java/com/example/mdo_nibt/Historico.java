package com.example.mdo_nibt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Historico extends AppCompatActivity {
    private RecyclerView rv_historico;
    private String usuarioID, ga_ministerio;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private Boolean isGA = false;
    private  ArrayList<String> listaDatas;
    private List<String> listaFormatada = new ArrayList<>();
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        getSupportActionBar().hide();
        iniciarComponentes();
        ctx = this;


    }

    private void iniciarComponentes(){
        rv_historico = findViewById(R.id.rv_historico);
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference getdata = banco_recuperar.collection("/NIBT/historicoUsuarios/datas").document(usuarioID);

        getdata.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    listaDatas = (ArrayList<String>)value.getData().get("data");

                    assert listaDatas != null;
                    for (String dataNumerica : listaDatas) {
                        String dataFormatada = formatarData(dataNumerica);
                        listaFormatada.add(dataFormatada);
                    }


                    HistoricoAdapter historicoAdapter = new HistoricoAdapter((ArrayList<String>) listaFormatada);
                    rv_historico.setAdapter(historicoAdapter);
                    rv_historico.setLayoutManager(new LinearLayoutManager(ctx));
                }
            }
        });
    }
    private static String formatarData(String data) {
        return data.substring(0, 2) + "/" + data.substring(2, 4) + "/" + data.substring(4);
    }
}