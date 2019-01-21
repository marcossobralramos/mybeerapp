package br.edu.ifba.mybeerapp.model;

public class Marca
{
    public int id;
    public String nome;

    public Marca(){}

    public Marca(int id, String nome) {
        this.id = id;
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
    public boolean equals(Object o)
    {
        if(o == this)
            return true;
        if(!(o instanceof Marca))
            return false;

        Marca marca = (Marca) o;

        return (this.id == marca.id && this.nome.equals(marca.nome));
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
