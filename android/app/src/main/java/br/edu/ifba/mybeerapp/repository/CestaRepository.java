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

    public int update(IModel modelOld, IModel modelNew) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        modelNew.setId(modelOld.getId());
        int result = super.update(modelOld, modelNew);

        if(result == -1)
            return -1;

        Cesta novaCesta = (Cesta) modelNew;
        Cesta antigaCesta = (Cesta) modelOld;

        for(int x = 0; x < novaCesta.getProdutos().size(); x++)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("cestaId", novaCesta.getId());
            contentValues.put("produtoId", novaCesta.getProdutos().get(x).getId());
            contentValues.put("qtdeProdutos", novaCesta.getProdutos().get(x).getQtde());

            return dbManager.getWritableDatabase().update(
                    this.table,
                    contentValues,
                    "cestaId = ? AND produtoId = ?",
                    new String[]{
                            String.valueOf(antigaCesta.getId()),
                            String.valueOf(antigaCesta.getProdutos().get(x).getId())
                    }
            );
        }

        return result;
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
                new String[]{"cestaId", "produtoId", "qtdeProdutos"},
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
            cesta.addProduto(produto, quantidade);

            dbResult.moveToNext();
        }

        return cesta;
    }

}
