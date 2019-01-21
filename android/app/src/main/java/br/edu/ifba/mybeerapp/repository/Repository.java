package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.db.DBManager;
import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public abstract class Repository
{
    protected SQLiteOpenHelper dbManager;
    protected String table;
    protected String defaultFieldOrderBy;
    protected String modelClassName;

    public Repository(Context context, String table, String defaultFieldOrderBy, String modelClassName)
    {
        this.dbManager = new DBManager(context);
        this.table = table;
        this.defaultFieldOrderBy = defaultFieldOrderBy;
        this.modelClassName = modelClassName;
    }

    public long create(Object model) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException
    {
        return dbManager.getWritableDatabase().insert(
                this.table,
                null,
                UtilsDB.getContentValues(model)
        );
    }

    public List<Object> retrieveAll() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException,
            ColunmTypeNotKnownException
    {
        String fieldsNames = UtilsDB.generateStringFields(this.modelClassName);

        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " ORDER BY " + this.defaultFieldOrderBy;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        return UtilsDB.createListModel(this.modelClassName, cursor);
    }

    public Object retrieveById(int id)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, ColunmTypeNotKnownException
    {
        String fieldsNames = UtilsDB.generateStringFields(this.modelClassName);
        String selectQuery = "SELECT " + fieldsNames + " FROM " + this.table + " WHERE id = " + id;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        return UtilsDB.createListModel(this.modelClassName, cursor).get(0);
    }

    public int update(Object model) throws IllegalAccessException, ColunmTypeNotKnownException,
            InvocationTargetException, NoSuchMethodException
    {
        ContentValues contentValues = UtilsDB.getContentValues(model);
        return dbManager.getWritableDatabase().update(
                this.table,
                contentValues,
                "id = ?",
                new String[]{contentValues.getAsString("id")}
        );
    }

    public int delete(int id) {
        return dbManager.getWritableDatabase().delete(
                this.table,
                "id = ?",
                new String[]{Integer.toString(id)}
        );
    }
}
