package br.edu.ifba.mybeerapp.model;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Modelo implements IModel
{
    private int id;
    private String nome;
    private Integer volume; // ml

    public Modelo(){}

    public Modelo(String nome, int volume) {
        this.nome = nome;
        this.volume = volume;
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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }
}
