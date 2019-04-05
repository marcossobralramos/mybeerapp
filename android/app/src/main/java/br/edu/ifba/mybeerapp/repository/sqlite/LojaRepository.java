package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;

public class LojaRepository extends Repository implements ILojaRepository
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
