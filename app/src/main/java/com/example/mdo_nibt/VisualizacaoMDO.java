package com.example.mdo_nibt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VisualizacaoMDO extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txt_titulo;
    private String data, dataFormatada, usuarioID, ga_ministerio, dataDesformatada;
    private ArrayList<VisualizacaoMDOModel> visualizacaoMDOModel = new ArrayList<>();
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private Boolean isGA = false;
    Context ctx;
//    private static List<mPessoaModel> listaPessoas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizacao_mdo);
        getSupportActionBar().hide();
        iniciarComponentes();
        ctx = this;

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("data");
            dataFormatada = Util.formatarData(data);
            txt_titulo.setText(dataFormatada);
            dataDesformatada = Util.desformatarData(dataFormatada);
        }
    }
    private void iniciarComponentes(){
        txt_titulo = findViewById(R.id.title_visualizacao_mdo);
        recyclerView = findViewById(R.id.rv_conteudo_historico);
    }
    private void setUpVizualizacaoMDOModels(){
        String[] nomePessoa = new String[]{"Jonathan", "Lucas", "Ana"};
        String[] meditacaoPessoa = new String[]{"5", "5", "5"};
        String[] decoracaoPessoa = new String[]{"1", "2", "3"};
        String[] oracaoPessoa = new String[]{"5", "4", "1"};

        for (int i =0; i<nomePessoa.length; i++){
        visualizacaoMDOModel.add(new VisualizacaoMDOModel(nomePessoa[i], meditacaoPessoa[i], decoracaoPessoa[i],oracaoPessoa[i]));
        }
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
                    if (isGA) {
                        DocumentReference getdata = banco_recuperar.collection("NIBT" + "/" + "GAs" + "/" + ga_ministerio + "/" + dataDesformatada + "/" + "MDOs").document(usuarioID);
                        getdata.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value != null) {
                                    setUpVizualizacaoMDOModels();

                                    VizualizacaoMDOAdapter adapter = new VizualizacaoMDOAdapter(ctx, (ArrayList<VisualizacaoMDOModel>) converterMapParaLista(value.getData()));
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                                }
                            }
                        });
                    }
                    else {
                        DocumentReference getdata = banco_recuperar.collection("NIBT" + "/" + "Ministerios" + "/" + ga_ministerio + "/" + dataDesformatada + "/" + "MDOs").document(usuarioID);
                        getdata.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value != null) {
                                    setUpVizualizacaoMDOModels();

                                    VizualizacaoMDOAdapter adapter = new VizualizacaoMDOAdapter(ctx, (ArrayList<VisualizacaoMDOModel>) converterMapParaLista(value.getData()));
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    public static List<VisualizacaoMDOModel> converterMapParaLista(Map<String, Object> mapa) {

        List<VisualizacaoMDOModel> listaVisualizacaoMDOModel = new ArrayList<>();

        if (mapa.containsKey("pessoas")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pessoas = (List<Map<String, Object>>) mapa.get("pessoas");

            for (Map<String, Object> pessoaMap : pessoas) {
                String nome = (String) pessoaMap.get("nome");
                String meditacao = (String) pessoaMap.get("meditacao");
                String decoracao = (String) pessoaMap.get("decoracao");
                String oracao = (String) pessoaMap.get("oracao");

                System.out.println("Nome: " + nome);
                System.out.println("Meditação: " + meditacao);
                System.out.println("Decoração: " + decoracao);
                System.out.println("Oração: " + oracao);
                System.out.println();

                VisualizacaoMDOModel visualizacaoMDOModel = new VisualizacaoMDOModel(nome, meditacao, decoracao, oracao);
                listaVisualizacaoMDOModel.add(visualizacaoMDOModel);
            }
        }
        return listaVisualizacaoMDOModel;
    }

}