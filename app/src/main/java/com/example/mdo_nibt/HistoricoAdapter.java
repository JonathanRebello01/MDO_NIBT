package com.example.mdo_nibt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private ArrayList<String> lista = new ArrayList();

    public HistoricoAdapter(ArrayList<String> lista){
        this.lista = lista;
    }

    class HistoricoViewHolder extends RecyclerView.ViewHolder {

        TextView dataMDO;
        ImageView imagemMDO;
        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            dataMDO = itemView.findViewById(R.id.TxtDate_historico);
            imagemMDO = itemView.findViewById(R.id.logo_gaministerio_historico);
        }

    }

    //Ao criar o View Holder
    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.item_historico, parent, false);

        return new HistoricoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        if (lista != null && position < lista.size()) {
            String data = lista.get(position);
            holder.dataMDO.setText(data);
        }

    }


    // getItemCount -> Recuperar a quantidade de itens
    @Override
    public int getItemCount() {
        return lista.size();
    }



}
