package br.edu.ifba.mybeerapp.model;

import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Loja implements IModel
{
    @DBField
    private int id;
    @DBField
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

    @Override
    public IModel clone() {
        return null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
            return true;
        if(!(o instanceof Loja))
            return false;

        Loja loja = (Loja) o;

        return (this.id == loja.id);
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.nome);
    }

}
