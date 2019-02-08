package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.db.DBManager;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public abstract class Repository
{
    protected SQLiteOpenHelper dbManager;
    protected String table;
    protected String defaultFieldOrderBy;
    protected String modelClassName;
    protected Context context;

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

    public long create(IModel model) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        ContentValues contentValues = UtilsDB.getContentValues(model);
        contentValues.remove("id");
        return dbManager.getWritableDatabase().insert(
                this.table,
                null,
                contentValues
        );
    }

    public List<IModel> retrieveAll() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        String fieldsNames = UtilsDB.generateStringFields(this.modelClassName);

        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table +
                " ORDER BY " + this.defaultFieldOrderBy;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        return UtilsDB.createListModel(this.modelClassName, cursor, this);
    }

    public IModel retrieveById(int id)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException
    {
        String fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " WHERE id = " + id;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        try
        {
            return UtilsDB.createListModel(this.modelClassName, cursor, this).get(0);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public int update(IModel modelOld, IModel modelNew) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, IOException, ClassNotFoundException
    {
        modelNew.setId(modelOld.getId());
        ContentValues contentValues = UtilsDB.getContentValues(modelNew);
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

    public Object retrieveModel(String typeName, String id) throws IllegalAccessException,
            InvocationTargetException, InstantiationException,
            NoSuchMethodException, ClassNotFoundException
    {
        Repository repository =
                (Repository) Class.forName(typeName.replace("model", "repository")
                        + "Repository").newInstance();

        repository.setContext(this.context);

        return repository.retrieveById(Integer.valueOf(id));
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
