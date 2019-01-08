package br.edu.ifba.mybeerapp.model;

public class Modelo
{
    private String id;
    private String descricao;
    private int volume; // ml

    public Modelo(String id, String descricao, int volume) {
        this.id = id;
        this.descricao = descricao;
        this.volume = volume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
