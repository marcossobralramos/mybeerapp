package br.edu.ifba.mybeerapp.model;

import java.util.Comparator;
import java.util.Date;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Produto implements IModel, Comparable<Produto>
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
    private double precoLitro;
    @DBField
    private String ultimaAtualizacao;

    private int cestaId;
    private int qtde;
    private double valorTotal;
    private double totalML;

    public Produto(){}

    public Produto(Loja loja, Bebida bebida, double precoUnidade, String ultimaAtualizacao) {
        this.loja = loja;
        this.bebida = bebida;
        this.precoUnidade = precoUnidade;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    @RepositoryNotAccess
    public double getTotalML() {
        return totalML;
    }

    @RepositoryNotAccess
    public void setTotalML(double totalML) {
        this.totalML = totalML;
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
    public int getCestaId() {
        return cestaId;
    }

    @RepositoryNotAccess
    public void setCestaId(int cestaId) {
        this.cestaId = cestaId;
    }

    @RepositoryNotAccess
    public int getQtde() {
        return qtde;
    }

    @RepositoryNotAccess
    public void setQtde(int qtde) {
        this.qtde = qtde;
        this.valorTotal = this.precoUnidade * qtde;
        this.totalML = this.bebida.getModelo().getVolume() * qtde;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public IModel clone() {
        return null;
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
        this.precoLitro = (this.precoUnidade/this.bebida.getModelo().getVolume()) * 1000;
    }

    public double getPrecoLitro() {
        return precoLitro;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public boolean equals(Object o)
    {
        return ((Produto) o).getId() == this.id;
    }

    public String toString()
    {
        return this.bebida.toString();
    }

    @Override
    public int compareTo(Produto o) {
        if(this.getPrecoLitro() > o.getPrecoLitro())
            return 1;
        else if(this.getPrecoLitro() < o.getPrecoLitro())
            return -1;
        return 0;
    }
}
