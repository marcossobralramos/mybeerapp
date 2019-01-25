package br.edu.ifba.mybeerapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Cesta implements IModel
{
    private int id;
    private String descricao;
    private double valorTotal;
    private Loja loja;
    private ArrayList<Produto> produtos;
    private Map<String, Integer> quantidadesProdutos;

    public Cesta(){
        this.produtos = new ArrayList<>();
        this.quantidadesProdutos = new HashMap<>();
    }

    public Cesta(String descricao, Double valorTotal, Loja loja, ArrayList<Produto> produtos, Map<String, Integer> quantidadesProdutos) {
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.loja = loja;
        this.produtos = produtos;
        this.quantidadesProdutos = quantidadesProdutos;
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

    public Map<String, Integer> getQuantidadesProdutos() {
        return quantidadesProdutos;
    }

    public void setQuantidadeProdutos(Map<String, Integer> quantidadesProdutos) {
        this.quantidadesProdutos = quantidadesProdutos;
    }

    public boolean addProduto(Produto produto)
    {
        boolean result = this.produtos.add(produto);

        if(result == true)
            this.valorTotal += produto.getPrecoUnidade();
        return false;
    }

    public boolean removeProduto(Produto produto)
    {
        return this.produtos.remove(produto);
    }

    public void setQuantidadeProduto(int idProduto, int quantidade)
    {
        this.quantidadesProdutos.put(String.valueOf(idProduto), quantidade);
    }

    public int getQuantidadeProduto(int idProduto)
    {
        return this.quantidadesProdutos.get(idProduto);
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
