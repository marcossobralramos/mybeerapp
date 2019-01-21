package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Modelo;

public class ModeloRepository extends Repository {

    public ModeloRepository(Context context)
    {
        super(context, "modelo", "nome", Modelo.class.getName());
    }
}
