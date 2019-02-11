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

        if(this.produtos.contains(produto))
            this.removeProduto(produto);

        boolean result = this.produtos.add(produto);

        if(result == true)
        {
            this.valorTotal += (produto.getPrecoUnidade() * qtde);
            this.totalLitros += (produto.getBebida().getModelo().getVolume() * qtde) / 1000;
        }

        return false;
    }

    public boolean removeProduto(Produto produto)
    {
        boolean result = this.produtos.remove(produto);
        if(result == true)
        {
            this.valorTotal -= (produto.getPrecoUnidade() * produto.getQtde());
            this.totalLitros += (produto.getBebida().getModelo().getVolume() * produto.getQtde()) / 1000;
        }
        return result;
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
