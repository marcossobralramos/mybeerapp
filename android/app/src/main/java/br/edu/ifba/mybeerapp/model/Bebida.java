package br.edu.ifba.mybeerapp.model;

import android.media.Image;

import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class Bebida implements IModel
{
    @DBField
    private int id;
    @DBField
    private Marca marca;
    @DBField
    private Modelo modelo;
    //private Image imagem;

    public Bebida(){}

    public Bebida(Marca marca, Modelo modelo /*, Image imagem*/) {
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
    public boolean equals(Object o)
    {
        if(o == this)
            return true;
        if(!(o instanceof Bebida))
            return false;

        Bebida bebida = (Bebida) o;

        return (this.id == bebida.id);
    }

    @Override
    public String toString()
    {
        return this.marca.getNome() + " - " + this.modelo.getNome() + " - " + this.modelo.getVolume() + "ml";
    }

}