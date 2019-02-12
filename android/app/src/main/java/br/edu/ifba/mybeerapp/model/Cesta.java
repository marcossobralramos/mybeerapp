package br.edu.ifba.mybeerapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Cesta implements IModel
{
    @DBField
    private int id;
    @DBField
    private String descricao;
    @DBField
    private double valorTotal;
    @DBField
    private ArrayList<Produto> produtos;

    private double totalLitros;

    public Cesta(){
        this.produtos = new ArrayList<>();
        this.valorTotal = 0;
        this.totalLitros = 0;
    }

    public Cesta(String descricao, Double valorTotal, ArrayList<Produto> produtos) {
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.produtos = produtos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public IModel clone() {
        Cesta clone = new Cesta();
        clone.setId(this.id);
        clone.setDescricao(this.descricao);
        clone.setProdutos((ArrayList<Produto>) this.produtos.clone());
        clone.setTotalLitros(this.totalLitros);
        clone.setValorTotal(this.valorTotal);
        return clone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @RepositoryNotAccess
    public double getValorTotal() {
        return valorTotal;
    }

    @RepositoryNotAccess
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
    public double getTotalLitros() {
        return totalLitros;
    }

    @RepositoryNotAccess
    public void setTotalLitros(double totalLitros) {
        this.totalLitros = totalLitros;
    }

    public boolean addProduto(Produto produto, int qtde)
    {
        if(produto == null)
            return false;

        produto.setQtde(qtde);
        produto.setCestaId(this.id);

        if(this.produtos.contains(produto))
            this.removeProduto(produto);

        boolean success = this.produtos.add(produto);

        if(success)
        {
            this.valorTotal += produto.getValorTotal();
            this.totalLitros += produto.getTotalML() / 1000;
        }

        return success;
    }

    public boolean removeProduto(Produto produto)
    {
        boolean success = this.produtos.remove(produto);
        if(success)
        {
            this.valorTotal -= (produto.getPrecoUnidade() * produto.getQtde());
            this.totalLitros += (produto.getBebida().getModelo().getVolume() * produto.getQtde()) / 1000;
        }
        return success;
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
