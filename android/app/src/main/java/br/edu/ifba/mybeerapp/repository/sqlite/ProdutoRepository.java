package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class ProdutoRepository extends Repository implements IProdutoRepository {
    public ProdutoRepository() {
        super("produto", "nome", Produto.class.getName());
    }

    public ProdutoRepository(Context context) {
        super(context, "produto", "lojaId", Produto.class.getName());
    }

    public List<IModel> retrieveByLoja(int lojaId) {
        String fieldsNames = null;
        try {
            fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " WHERE lojaId = " + lojaId;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        try {
            return UtilsDB.createListModel(this.modelClassName, cursor, this);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
