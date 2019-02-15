package br.edu.ifba.mybeerapp.repository;

import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

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

    public List<IModel> retrieveByLoja(int lojaId)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException
    {
        String fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " WHERE lojaId = " + lojaId;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        try
        {
            return UtilsDB.createListModel(this.modelClassName, cursor, this);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }
}
