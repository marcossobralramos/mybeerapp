package br.edu.ifba.mybeerapp.model;

import java.util.ArrayList;
import java.util.Map;

public class Cesta
{
    private int id;
    private String descricao;
    private Double valorTotal;
    private Loja loja;
    private ArrayList<Produto> produtos;
    private Map<String, Integer> quantidade;

    public Cesta(){}

    public Cesta(int id, String descricao, Double valorTotal, Loja loja, ArrayList<Produto> produtos, Map<String, Integer> quantidade) {
        this.id = id;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.loja = loja;
        this.produtos = produtos;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public Map<String, Integer> getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Map<String, Integer> quantidade) {
        this.quantidade = quantidade;
    }
}
