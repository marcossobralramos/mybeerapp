package br.edu.ifba.mybeerapp.model;

import android.media.Image;

public class Bebida
{
    private int id;
    private Marca marca;
    private Modelo modelo;
    //private Image imagem;

    public Bebida(int id, Marca marca, Modelo modelo /*, Image imagem*/) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        //this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    /*public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }*/

    @Override
    public String toString()
    {
        return String.valueOf(this.id);
    }

}