package br.edu.ifba.mybeerapp.repository.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import br.edu.ifba.mybeerapp.db.DBManager;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class Repository implements IRepository
{
    protected SQLiteOpenHelper dbManager;
    protected String table;
    protected String defaultFieldOrderBy;
    protected String modelClassName;
    protected Context context;
    protected ViewCallback viewCallback;

    public Repository(String table, String defaultFieldOrderBy, String modelClassName)
    {
        this.dbManager = new DBManager(context);
        this.table = table;
        this.defaultFieldOrderBy = defaultFieldOrderBy;
        this.modelClassName = modelClassName;
    }

    public Repository(Context context, String table, String defaultFieldOrderBy, String modelClassName)
    {
        this.dbManager = new DBManager(context);
        this.table = table;
        this.defaultFieldOrderBy = defaultFieldOrderBy;
        this.modelClassName = modelClassName;
        this.context = context;
    }

    public long create(IModel model)
    {
        ContentValues contentValues = null;
        try {
            contentValues = UtilsDB.getContentValues(model);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        contentValues.remove("id");
        return dbManager.getWritableDatabase().insert(
                this.table,
                null,
                contentValues
        );
    }

    public List<IModel> retrieveAll()
    {
        String fieldsNames = null;
        try {
            fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table +
                " ORDER BY " + this.defaultFieldOrderBy;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        try {
            return UtilsDB.createListModel(this.modelClassName, cursor, this);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IModel retrieveById(int id)
    {
        String fieldsNames = null;
        try {
            fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " WHERE id = " + id;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        try
        {
            try {
                return UtilsDB.createListModel(this.modelClassName, cursor, this).get(0);
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                    InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
        return null;
    }

    public int update(IModel modelOld, IModel modelNew)
    {
        modelNew.setId(modelOld.getId());
        ContentValues contentValues = null;
        try {
            contentValues = UtilsDB.getContentValues(modelNew);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return dbManager.getWritableDatabase().update(
                this.table,
                contentValues,
                "id = ?",
                new String[]{String.valueOf(modelOld.getId())}
        );
    }

    public int delete(int id) {
        return dbManager.getWritableDatabase().delete(
                this.table,
                "id = ?",
                new String[]{Integer.toString(id)}
        );
    }

    public Object retrieveModel(String typeName, String id)
    {
        RepositoryLoader loader = RepositoryLoader.getInstance();
        Method method = null;
        try {
            method = loader.getClass().getMethod("get" + typeName + "Repository", Context.class);
            IRepository repository = (IRepository) method.invoke(loader, this.context);
            return repository.retrieveById(Integer.valueOf(id));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ViewCallback getViewCallback() {
        return viewCallback;
    }

    @Override
    public void setViewCallback(ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    public void setContext(Context context)
    {
        this.context = context;
        this.dbManager = new DBManager(context);
    }

    protected int getNextId()
    {
        String query = "SELECT MAX(id) AS max_id FROM " + this.table;
        Cursor cursor = dbManager.getWritableDatabase().rawQuery(query, null);

        int id = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        return id;
    }
}