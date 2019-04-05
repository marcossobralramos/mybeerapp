package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.repository.interfaces.IModeloRepository;

public class ModeloRepository extends Repository implements IModeloRepository
{
    public ModeloRepository(){
        super("modelo", "nome", Modelo.class.getName());
    }

    public ModeloRepository(Context context)
    {
        super(context, "modelo", "nome", Modelo.class.getName());
    }
}
