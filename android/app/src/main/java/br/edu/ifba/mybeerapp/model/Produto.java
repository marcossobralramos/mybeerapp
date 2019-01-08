package br.edu.ifba.mybeerapp.model;

import java.util.Date;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Bebida;

public class Produto
{
    private int id;
    private Loja loja;
    private Bebida bebida;
    private Double preco;
    private Date ultimaAtualizacao;

    public Produto(int id, Loja loja, Bebida bebida, Double preco, Date ultimaAtualizacao) {
        this.id = id;
        this.loja = loja;
        this.bebida = bebida;
        this.preco = preco;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(Date ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
