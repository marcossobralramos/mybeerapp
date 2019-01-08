package br.edu.ifba.mybeerapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper
{
    public DBManager(Context context)
    {
        super(context, NOME_BASE_DE_DADOS,null, VERSAO_BASE_DE_DADOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_MARCA_TABLE);
        db.execSQL(CREATE_MODELO_TABLE);
        db.execSQL(CREATE_LOJA_TABLE);
        db.execSQL(CREATE_BEBIDA_TABLE);
        db.execSQL(CREATE_PRODUTO_TABLE);
        db.execSQL(CREATE_CESTA_TABLE);
        db.execSQL(CREATE_PRODUTOS_CESTAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    // DB info
    private static final String NOME_BASE_DE_DADOS = "MYBEERAPP.db";
    private static final int VERSAO_BASE_DE_DADOS = 1;

    // create tables
    private static final String CREATE_MARCA_TABLE = "CREATE TABLE marca (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL)";

    private static final String CREATE_MODELO_TABLE = "CREATE TABLE modelo (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, volume INTEGER NOT NULL)";

    private static final String CREATE_LOJA_TABLE = "CREATE TABLE loja (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL)";

    private static final String CREATE_BEBIDA_TABLE = "CREATE TABLE bebida (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, BLOB foto, marca_id INTEGER NOT NULL, " +
            "modelo_id INTEGER NOT NULL, FOREIGN KEY(marca_id) REFERENCES marca(id)," +
            "FOREIGN KEY(modelo_id) REFERENCES modelo(id))";

    private static final String CREATE_PRODUTO_TABLE = "CREATE TABLE produto (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, loja_id INTEGER NOT NULL, " +
            "bebida_id INTEGER NOT NULL, preco_unidade REAL(8,2) NOT NULL, " +
            "preco_ml REAL(8,2) NOT NULL, ultima_atualizacao TEXT, " +
            "FOREIGN KEY(loja_id) REFERENCES loja(id)," +
            "FOREIGN KEY(bebida_id) REFERENCES bebida(id))";

    private static final String CREATE_CESTA_TABLE = "CREATE TABLE cesta (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT NOT NULL, " +
            "valor_total REAL(8,2))";

    private static final String CREATE_PRODUTOS_CESTAS_TABLE = "CREATE TABLE produtos_cestas (" +
            "produto_id INTEGER NOT NULL, cesta_id INTEGER NOT NULL, " +
            "qtde_produtos INTEGER NOT NULL)";

}