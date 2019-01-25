package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Loja;

public class LojaRepository extends Repository
{
    public LojaRepository()
    {
        super("loja", "nome", Loja.class.getName());
    }
    public LojaRepository(Context context)
    {
        super(context, "loja", "nome", Loja.class.getName());
    }
}
