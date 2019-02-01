package br.edu.ifba.mybeerapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.db.DBManager;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;


@RunWith(AndroidJUnit4.class)
public class SQLiteTest
{
    private Context appContext;

    public SQLiteTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void genericTest()
    {
        DBManager dbManager = new DBManager(this.appContext);

        /*ContentValues contentValues = new ContentValues();
        contentValues.put("cestaId", 7);
        contentValues.put("produtoId", 15);
        contentValues.put("qtdeProdutos", 10);

        long result = dbManager.getWritableDatabase().insert(
                "produtos_cestas",
                null,
                contentValues
        );*/

        dbManager.getWritableDatabase().rawQuery("INSERT INTO produtos_cestas (cestaId, produtoId, qtdeProdutos) VALUES (7,15,10)", null);
        Cursor cursor = dbManager.getWritableDatabase().rawQuery("SELECT * FROM produtos_cestas", null);

        if(cursor == null)
            return;

    }

}
