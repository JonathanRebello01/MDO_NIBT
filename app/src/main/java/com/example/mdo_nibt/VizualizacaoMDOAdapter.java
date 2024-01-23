package com.example.mdo_nibt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VizualizacaoMDOAdapter extends RecyclerView.Adapter<VizualizacaoMDOAdapter.MyViewHolder> {
    Context context;
    ArrayList<VisualizacaoMDOModel> visualizacaoMDOModels;


    public VizualizacaoMDOAdapter(Context context, ArrayList<VisualizacaoMDOModel> visualizacaoMDOModels){
        this.context = context;
        this.visualizacaoMDOModels = visualizacaoMDOModels;
    }

    @NonNull
    @Override
    public VizualizacaoMDOAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_visualizacao_mdo, parent, false);
        return new VizualizacaoMDOAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VizualizacaoMDOAdapter.MyViewHolder holder, int position) {
        holder.nomePessoa.setText(visualizacaoMDOModels.get(position).getNome());
        holder.meditacaoPessoa.setText(visualizacaoMDOModels.get(position).getMeditacao());
        holder.decoracaoPessoa.setText(visualizacaoMDOModels.get(position).getDecoracao());
        holder.oracaoPessoa.setText(visualizacaoMDOModels.get(position).getOracao());
    }

    @Override
    public int getItemCount() {
        return visualizacaoMDOModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomePessoa;

        EditText meditacaoPessoa, decoracaoPessoa, oracaoPessoa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomePessoa = itemView.findViewById(R.id.nome_pessoa_vizializacao_mdo);
            meditacaoPessoa = itemView.findViewById(R.id.edt_meditacao_vizualizacao_mdo);
            decoracaoPessoa = itemView.findViewById(R.id.edt_decoracao_vizualizacao_mdo);
            oracaoPessoa = itemView.findViewById(R.id.edt_oracao_pessoa_vizualizacao_mdo);

        }
    }

}
