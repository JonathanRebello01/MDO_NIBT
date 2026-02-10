package com.example.mdo_nibt;

public class mPessoaModel {
    public String nome;

    public boolean faltou;
    public  boolean atrasou;

    public mPessoaModel(String nome, String meditacao, String decoracao, String oracao, boolean faltou, boolean atrasou) {
        this.nome = nome;
        this.meditacao = meditacao;
        this.decoracao = decoracao;
        this.oracao = oracao;
        this.faltou = faltou;
        this.atrasou = atrasou;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMeditacao() {
        return meditacao;
    }

    public void setMeditacao(String meditacao) {
        this.meditacao = meditacao;
    }

    public String getDecoracao() {
        return decoracao;
    }

    public void setDecoracao(String decoracao) {
        this.decoracao = decoracao;
    }

    public String getOracao() {
        return oracao;
    }

    public void setOracao(String oracao) {
        this.oracao = oracao;
    }

    public String meditacao;
    public String decoracao;
    public String oracao;
}
