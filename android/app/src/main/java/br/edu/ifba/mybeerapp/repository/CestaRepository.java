package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public class CestaRepository extends Repository
{
    private String tableToSaveProdutosCesta;

    public CestaRepository()
    {
        super("cesta", "descricao", Cesta.class.getName());
        this.tableToSaveProdutosCesta = "produtos_cestas";
    }

    public CestaRepository(Context context)
    {
        super(context, "cesta", "descricao", Cesta.class.getName());
        this.tableToSaveProdutosCesta = "produtos_cestas";
    }

    public long addProduto(Produto produto, int cestaId) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cestaId", cestaId);
        contentValues.put("produtoId", produto.getId());
        contentValues.put("preco", produto.getPrecoUnidade());

        Cesta cesta = (Cesta) this.retrieveById(cestaId);

        if(cesta.getProdutos().contains(produto))
        {
            int pos = cesta.getProdutos().indexOf(produto);
            contentValues.put("qtdeProdutos", produto.getQtde());
            return dbManager.getWritableDatabase().update(
                    this.tableToSaveProdutosCesta,
                    contentValues,
                    "cestaId = ? AND produtoId = ?",
                    new String[]{
                            String.valueOf(cestaId),
                            String.valueOf(produto.getId())
                    }
            );
        }
        else
        {
            contentValues.put("qtdeProdutos", produto.getQtde());
            return dbManager.getWritableDatabase().insert(
                    this.tableToSaveProdutosCesta,
                    null,
                    contentValues
            );
        }
    }

    public long create(IModel model) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        long id = super.create(model);

        if(id == -1)
            return -1;

        Cesta cesta = (Cesta) model;

        for(int x = 0; x < cesta.getProdutos().size(); x++)
        {
            int idProduto = cesta.getProdutos().get(x).getId();

            ContentValues contentValues = new ContentValues();
            contentValues.put("cestaId", id);
            contentValues.put("produtoId", idProduto);
            contentValues.put("qtdeProdutos", cesta.getProduto(idProduto).getQtde());
            contentValues.put("preco", cesta.getProduto(idProduto).getPrecoUnidade());

            long result = dbManager.getWritableDatabase().insert(
                    this.tableToSaveProdutosCesta,
                    null,
                    contentValues
            );

            if(result == -1)
                break;
        }

        return id;

    }

    public List<IModel> retrieveAll() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        List<IModel> cestas = super.retrieveAll();

        List<IModel> cestasCarregadas = new ArrayList<>();

        for(IModel cesta : cestas)
            cestasCarregadas.add(this.loadProdutosCesta((Cesta) cesta));

        return cestasCarregadas;
    }

    public IModel retrieveById(int id)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException
    {
        Cesta cesta = (Cesta) super.retrieveById(id);
        return loadProdutosCesta(cesta);
    }

    public int deleteProduto(int cestaId, int produtoId) {
        return super.dbManager.getWritableDatabase().delete(
                this.tableToSaveProdutosCesta,
                "cestaId = ? AND produtoId = ?",
                new String[]{String.valueOf(cestaId), String.valueOf(produtoId)}
        );
    }

    public int delete(int id) {
        return super.dbManager.getWritableDatabase().delete(
                super.table,
                "id = ?",
                new String[]{Integer.toString(id)}
        );
    }

    private Cesta loadProdutosCesta(Cesta cesta) throws IllegalAccessException,
            InvocationTargetException, InstantiationException,
            NoSuchMethodException, ClassNotFoundException
    {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.context);

        Cursor dbResult = dbManager.getWritableDatabase().query(
                this.tableToSaveProdutosCesta,
                new String[]{"cestaId", "produtoId", "qtdeProdutos", "preco"},
                "cestaId = ?",
                new String[]{String.valueOf(cesta.getId())},
                null, null, null, null
        );
        dbResult.moveToFirst();

        while(!dbResult.isAfterLast())
        {
            int produtoId = dbResult.getInt(dbResult.getColumnIndex("produtoId"));
            Produto produto = (Produto) produtoRepository.retrieveById(produtoId);
            int quantidade = dbResult.getInt(dbResult.getColumnIndex("qtdeProdutos"));
            double preco = dbResult.getDouble(dbResult.getColumnIndex("preco"));
            produto.setPrecoUnidade(preco);
            cesta.addProduto(produto, quantidade);

            dbResult.moveToNext();
        }

        return cesta;
    }

}