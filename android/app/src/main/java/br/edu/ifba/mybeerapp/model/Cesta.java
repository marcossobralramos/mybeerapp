package br.edu.ifba.mybeerapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Cesta implements IModel
{
    private int id;
    private String descricao;
    private double valorTotal;
    private ArrayList<Produto> produtos;
    private Map<String, Integer> quantidadesProdutos;

    public Cesta(){
        this.produtos = new ArrayList<>();
        this.quantidadesProdutos = new HashMap<>();
        this.valorTotal = 0;
    }

    public Cesta(String descricao, Double valorTotal, ArrayList<Produto> produtos, Map<String, Integer> quantidadesProdutos) {
        this.descricao = descricao;
        this.valorTotal = valorTotal;
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

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    @RepositoryNotAccess
    public Produto getProduto(int idProd)
    {
        for(Produto produto : this.produtos)
            if(produto.getId() == idProd)
                return produto;

        return null;
    }

    @RepositoryNotAccess
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    @RepositoryNotAccess
    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    @RepositoryNotAccess
    public Map<String, Integer> getQuantidadesProdutos() {
        return quantidadesProdutos;
    }

    @RepositoryNotAccess
    public void setQuantidadeProdutos(Map<String, Integer> quantidadesProdutos) {
        this.quantidadesProdutos = quantidadesProdutos;
    }

    public boolean addProduto(Produto produto, int qtde)
    {
        if(produto == null)
            return false;

        boolean result = this.produtos.add(produto);
        this.setQuantidadeProduto(produto.getId(), qtde);

        if(result == true)
            this.valorTotal += (produto.getPrecoUnidade() * this.quantidadesProdutos.get(String.valueOf(produto.getId())));

        return false;
    }

    public boolean removeProduto(Produto produto)
    {
        return this.produtos.remove(produto);
    }

    @RepositoryNotAccess
    public void setQuantidadeProduto(int idProduto, int quantidade)
    {
        this.quantidadesProdutos.put(String.valueOf(idProduto), quantidade);
    }

    @RepositoryNotAccess
    public int getQuantidadeProduto(int idProduto)
    {
        return this.quantidadesProdutos.get(String.valueOf(idProduto));
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
