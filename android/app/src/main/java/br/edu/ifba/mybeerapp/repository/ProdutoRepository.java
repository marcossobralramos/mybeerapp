package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.Produto;

public class ProdutoRepository extends Repository
{
    public ProdutoRepository()
    {
        super("produto", "nome", Produto.class.getName());
    }

    public ProdutoRepository(Context context)
    {
        super(context, "produto", "lojaId", Produto.class.getName());
    }
}
