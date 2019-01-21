package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

import br.edu.ifba.mybeerapp.model.Produto;

public class ProdutoRepository extends Repository
{
    public ProdutoRepository(Context context)
    {
        super(context, "produto", "nome", Produto.class.getName());
    }
}
