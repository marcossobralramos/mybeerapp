package br.edu.ifba.mybeerapp.repository;

import android.content.Context;

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
