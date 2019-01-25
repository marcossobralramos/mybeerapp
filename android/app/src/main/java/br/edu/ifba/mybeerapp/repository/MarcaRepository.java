package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Marca;

public class MarcaRepository extends Repository
{
    public MarcaRepository(){
        super("marca", "nome", Marca.class.getName());
    }

    public MarcaRepository(Context context)
    {
        super(context, "marca", "nome", Marca.class.getName());
    }
}