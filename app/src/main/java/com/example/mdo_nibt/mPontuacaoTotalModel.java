package com.example.mdo_nibt;

import androidx.annotation.NonNull;

public class mPontuacaoTotalModel {
    public String pontuacao_dimmer_total, pontuacao_canon_total, pontuacao_delay_total;

    public mPontuacaoTotalModel(String pontuacao_dimmer_total, String pontuacao_canon_total, String pontuacao_delay_total) {
        this.pontuacao_dimmer_total = pontuacao_dimmer_total;
        this.pontuacao_canon_total = pontuacao_canon_total;
        this.pontuacao_delay_total = pontuacao_delay_total;
    }

    public String getPontuacao_dimmer_total() {
        return pontuacao_dimmer_total;
    }

    public void setPontuacao_dimmer_total(String pontuacao_dimmer_total) {
        this.pontuacao_dimmer_total = pontuacao_dimmer_total;
    }

    public String getPontuacao_canon_total() {
        return pontuacao_canon_total;
    }

    public void setPontuacao_canon_total(String pontuacao_canon_total) {
        this.pontuacao_canon_total = pontuacao_canon_total;
    }

    public String getPontuacao_delay_total() {
        return pontuacao_delay_total;
    }

    public void setPontuacao_delay_total(String pontuacao_delay_total) {
        this.pontuacao_delay_total = pontuacao_delay_total;
    }

    @NonNull
    @Override
    public String toString() {
        return "mPontuacaoTotalModel{" +
                "pontuacao_dimmer_total='" + pontuacao_dimmer_total + '\'' +
                ", pontuacao_canon_total='" + pontuacao_canon_total + '\'' +
                ", pontuacao_delay_total='" + pontuacao_delay_total + '\'' +
                '}';
    }
}
