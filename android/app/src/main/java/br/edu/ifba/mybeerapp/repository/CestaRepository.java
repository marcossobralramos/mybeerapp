package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class CestaRepository extends Repository
{
    private String tableToSaveProdutosCesta;

    public CestaRepository(Context context)
    {
        super(context, "cesta", "loja_id", Cesta.class.getName());
        this.tableToSaveProdutosCesta = "produtos_cestas";
    }

    public long create(Object model) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException
    {
        long result = super.create(model);

        if(result <= 0)
            return result;

        dbManager.getWritableDatabase().beginTransaction();

        try
        {
            Cesta cesta = (Cesta) model;

            for(int x = 0; x < cesta.getProdutos().size(); x++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put("cesta_id", cesta.getId());
                contentValues.put("produto_id", cesta.getProdutos().get(x).getId());
                contentValues.put("qtde_produtos", cesta.getQuantidadeProdutos().get(x));

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

        return result;

    }

    public List<Object> retrieveAll() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException,
            ColunmTypeNotKnownException
    {
        String fieldsNames = UtilsDB.generateStringFields(super.modelClassName);

        String selectQuery = "SELECT " + fieldsNames + " FROM " + super.table + " ORDER BY " + super.defaultFieldOrderBy;

        Cursor cursor = super.dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        return UtilsDB.createListModel(super.modelClassName, cursor);
    }

    public Object retrieveById(int id)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, ColunmTypeNotKnownException
    {
        String fieldsNames = UtilsDB.generateStringFields(super.modelClassName);
        String selectQuery = "SELECT " + fieldsNames + " FROM " + super.table + " WHERE id = " + id;

        Cursor cursor = super.dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        return UtilsDB.createListModel(super.modelClassName, cursor).get(0);
    }

    public int update(Object model) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException
    {
        ContentValues contentValues = UtilsDB.getContentValues(model);
        return super.dbManager.getWritableDatabase().update(
                super.table,
                contentValues,
                "id = ?",
                new String[]{contentValues.getAsString("id")}
        );
    }

    public int delete(int id) {
        return super.dbManager.getWritableDatabase().delete(
                super.table,
                "id = ?",
                new String[]{Integer.toString(id)}
        );
    }

}
