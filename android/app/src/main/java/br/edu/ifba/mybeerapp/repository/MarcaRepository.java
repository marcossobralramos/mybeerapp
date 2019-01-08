package br.edu.ifba.mybeerapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.db.DBManager;
import br.edu.ifba.mybeerapp.model.Marca;

public class MarcaRepository
{
    private SQLiteOpenHelper dbManager;

    public MarcaRepository(Context context)
    {
        this.dbManager = new DBManager(context);
    }

    public long create(Marca marca)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", marca.getId());
        contentValues.put("nome", marca.getNome());

        return dbManager.getWritableDatabase().insert("marca", null, contentValues);
    }

    public List<Marca> retrieveAll()
    {
        List<Marca> marcas = new ArrayList<Marca>();

        String selectQuery = "SELECT id, nome FROM marca ORDER BY nome";

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);

        cursor.moveToFirst();

        Marca marca;

        while(!cursor.isAfterLast())
        {
            marca = new Marca();

            marca.setId(cursor.getInt(cursor.getColumnIndex("id")));
            marca.setNome(cursor.getString(cursor.getColumnIndex("nome")));

            marcas.add(marca);

            cursor.moveToNext();
        }

        return marcas;
    }

    public Marca retrieveById(int id)
    {
        String selectQuery = "SELECT id, nome FROM marca WHERE id = " + id;

        Cursor cursor = dbManager.getWritableDatabase().rawQuery(selectQuery, null);
        cursor.moveToFirst();

        Marca marca = new Marca();
        marca.setId(cursor.getInt(cursor.getColumnIndex("id")));
        marca.setNome(cursor.getString(cursor.getColumnIndex("nome")));

        return marca;
    }

    public int update(Marca marca)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", marca.getId());
        contentValues.put("nome", marca.getNome());

        return dbManager.getWritableDatabase().update(
            "marca",
            contentValues,
            "id = ?",
            new String[]{Integer.toString(marca.getId())}
        );
    }

    public int delete(int id)
    {
        return dbManager.getWritableDatabase().delete(
            "marca",
            "id = ?",
            new String[]{Integer.toString(id)}
        );
    }

}