package com.example.mdo_nibt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.EventListener;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<String> lista = new ArrayList();

    public HistoricoAdapter(ArrayList<String> lista, RecyclerViewInterface recyclerViewInterface){
        this.lista = lista;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //Ao criar o View Holder
    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.item_historico, parent, false);

        return new HistoricoViewHolder(itemView, recyclerViewInterface);
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

    public static class HistoricoViewHolder extends RecyclerView.ViewHolder {

        Button dataMDO;
        ImageView imagemMDO;
        public HistoricoViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            dataMDO = itemView.findViewById(R.id.btn_data_historico);
            imagemMDO = itemView.findViewById(R.id.logo_gaministerio_historico);
            dataMDO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }

}
