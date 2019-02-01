package br.edu.ifba.mybeerapp.model;

import java.util.Date;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Produto implements IModel
{
    private int id;
    private Loja loja;
    private Bebida bebida;
    private double precoUnidade;
    private double precoML;
    private String ultimaAtualizacao;

    public Produto(){}

    public Produto(Loja loja, Bebida bebida, double precoUnidade, String ultimaAtualizacao) {
        this.loja = loja;
        this.bebida = bebida;
        this.precoUnidade = precoUnidade;
        this.ultimaAtualizacao = ultimaAtualizacao;
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
        return String.valueOf(this.id);
    }
}
