package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Bebida;

public class BebidaRepository extends Repository
{
    public BebidaRepository()
    {
        super("bebida", "marcaId", Bebida.class.getName());
    }

    public BebidaRepository(Context context)
    {
        super(context, "bebida", "marcaId", Bebida.class.getName());
    }
}