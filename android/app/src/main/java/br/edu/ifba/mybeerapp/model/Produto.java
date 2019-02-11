package br.edu.ifba.mybeerapp.model;

import java.util.Date;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Produto implements IModel
{
    @DBField
    private int id;
    @DBField
    private Loja loja;
    @DBField
    private Bebida bebida;
    @DBField
    private double precoUnidade;
    @DBField
    private double precoML;
    @DBField
    private String ultimaAtualizacao;

    private int qtde;

    public Produto(){}

    public Produto(Loja loja, Bebida bebida, double precoUnidade, String ultimaAtualizacao) {
        this.loja = loja;
        this.bebida = bebida;
        this.precoUnidade = precoUnidade;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    @RepositoryNotAccess
    public int getQtde() {
        return qtde;
    }

    @RepositoryNotAccess
    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public Bebida getBebida() {
        return bebida;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }

    public double getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(double precoUnidade) {
        this.precoUnidade = precoUnidade;
        this.precoML = this.precoUnidade/this.bebida.getModelo().getVolume();
    }

    public double getPrecoML() {
        return precoML;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public boolean equals(Object o)
    {
        if(((Produto) o).getId() == this.getId())
            return true;
        return false;
    }

    public String toString()
    {
        return this.bebida.toString();
    }
}
