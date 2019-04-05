package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IMarcaRepository;

public class MarcaRepository extends Repository implements IMarcaRepository
{
    public MarcaRepository(){
        super("marca", "nome", Marca.class.getName());
    }

    public MarcaRepository(Context context)
    {
        super(context, "marca", "nome", Marca.class.getName());
    }

}