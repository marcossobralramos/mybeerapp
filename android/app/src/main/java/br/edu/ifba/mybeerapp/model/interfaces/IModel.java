package br.edu.ifba.mybeerapp.model.interfaces;

public interface IModel {
    int getId();
    void setId(int id);
    IModel clone();
}
