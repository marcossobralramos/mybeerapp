package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;

public class BebidaRepository extends Repository implements IBebidaRepository
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