package br.edu.ifba.mybeerapp.model;

public class Modelo
{
    private Integer id;
    private String descricao;
    private Integer volume; // ml

    public Modelo(){}

    public Modelo(Integer id, String descricao, int volume) {
        this.id = id;
        this.descricao = descricao;
        this.volume = volume;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
