package com.example.mdo_nibt;

public class mPontuacaoParcialModel {
    public String pontuacao_dimmer_parcial, pontuacao_canon_parrcial, pontuacao_delay_parcial;

    public mPontuacaoParcialModel(String pontuacao_dimmer_parcial, String pontuacao_canon_parrcial, String pontuacao_delay_parcial) {
        this.pontuacao_dimmer_parcial = pontuacao_dimmer_parcial;
        this.pontuacao_canon_parrcial = pontuacao_canon_parrcial;
        this.pontuacao_delay_parcial = pontuacao_delay_parcial;
    }


    public String getPontuacao_dimmer_parcial() {
        return pontuacao_dimmer_parcial;
    }

    public void setPontuacao_dimmer_parcial(String pontuacao_dimmer_parcial) {
        this.pontuacao_dimmer_parcial = pontuacao_dimmer_parcial;
    }

    public String getPontuacao_canon_parrcial() {
        return pontuacao_canon_parrcial;
    }

    public void setPontuacao_canon_parrcial(String pontuacao_canon_parrcial) {
        this.pontuacao_canon_parrcial = pontuacao_canon_parrcial;
    }

    public String getPontuacao_delay_parcial() {
        return pontuacao_delay_parcial;
    }

    public void setPontuacao_delay_parcial(String pontuacao_delay_parcial) {
        this.pontuacao_delay_parcial = pontuacao_delay_parcial;
    }
}
