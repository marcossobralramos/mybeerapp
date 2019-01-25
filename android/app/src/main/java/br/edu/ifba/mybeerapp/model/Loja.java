package br.edu.ifba.mybeerapp.model;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Loja implements IModel
{
    private int id;
    private String nome;

    public Loja(){}

    public Loja(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.id);
    }

}
