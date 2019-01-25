package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class CestaRepository extends Repository
{
    private String tableToSaveProdutosCesta;

    public CestaRepository()
    {
        super("cesta", "lojaId", Cesta.class.getName());
        this.tableToSaveProdutosCesta = "produtos_cestas";
    }

    public CestaRepository(Context context)
    {
        super(context, "cesta", "lojaId", Cesta.class.getName());
        this.tableToSaveProdutosCesta = "produtos_cestas";
    }

    public long create(IModel model) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        long id = super.create(model);

        if(id == -1)
            return -1;

        dbManager.getWritableDatabase().beginTransaction();

        try
        {
            Cesta cesta = (Cesta) model;

            for(int x = 0; x < cesta.getProdutos().size(); x++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put("cestaId", cesta.getId());
                contentValues.put("produtoId", cesta.getProdutos().get(x).getId());
                contentValues.put("qtdeProdutos", cesta.getQuantidadesProdutos().get(x));

                dbManager.getWritableDatabase().insert(
                        this.tableToSaveProdutosCesta,
                        null,
                        contentValues
                );
            }

            dbManager.getWritableDatabase().setTransactionSuccessful();
        }
        catch (Exception e)
        {
            //
        }
        finally
        {
            dbManager.getWritableDatabase().endTransaction();
        }


        dbManager.getWritableDatabase().endTransaction();

        return id;

    }

    public List<IModel> retrieveAll() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException,
            ColunmTypeNotKnownException
    {
        List<IModel> cestas = super.retrieveAll();

        List<IModel> cestasCarregadas = new ArrayList<>();

        for(IModel cesta : cestas)
            cestasCarregadas.add(this.loadProdutosCesta((Cesta) cesta));

        return cestasCarregadas;
    }

    public IModel retrieveById(int id)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, ColunmTypeNotKnownException
    {
        Cesta cesta = (Cesta) super.retrieveById(id);
        return loadProdutosCesta(cesta);
    }

    public int update(IModel modelOld, IModel modelNew) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        int result = super.update(modelOld, modelNew);

        if(result == -1)
            return -1;

        dbManager.getWritableDatabase().beginTransaction();

        try
        {
            Cesta novaCesta = (Cesta) modelNew;
            Cesta antigaCesta = (Cesta) modelOld;

            for(int x = 0; x < novaCesta.getProdutos().size(); x++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put("cestaId", novaCesta.getId());
                contentValues.put("produtoId", novaCesta.getProdutos().get(x).getId());
                contentValues.put("qtdeProdutos", novaCesta.getQuantidadesProdutos().get(x));

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

            dbManager.getWritableDatabase().setTransactionSuccessful();
        }
        catch (Exception e)
        {
            //
        }
        finally
        {
            dbManager.getWritableDatabase().endTransaction();
        }


        dbManager.getWritableDatabase().endTransaction();

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
            InvocationTargetException, InstantiationException, ColunmTypeNotKnownException,
            NoSuchMethodException, ClassNotFoundException
    {
        String selectQuery = "SELECT produtoId FROM " + this.tableToSaveProdutosCesta +
                "WHERE cestaId = " + cesta.getId();

        ProdutoRepository produtoRepository = new ProdutoRepository(this.context);

        Cursor dbResult = dbManager.getWritableDatabase().rawQuery(selectQuery, null);
        dbResult.moveToFirst();

        while(!dbResult.isAfterLast())
        {
            int produtoId = dbResult.getInt(dbResult.getColumnIndex("produtoId"));
            Produto produto = (Produto) produtoRepository.retrieveById(produtoId);
            cesta.addProduto(produto);

            int quantidade = dbResult.getInt(dbResult.getColumnIndex("qtdeProdutos"));
            cesta.setQuantidadeProduto(produtoId, quantidade);
        }

        return cesta;
    }

}
