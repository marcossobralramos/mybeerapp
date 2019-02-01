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
        db.execSQL("DROP TABLE IF EXISTS produtos_cestas");
        db.execSQL("DROP TABLE IF EXISTS cesta");
        db.execSQL("DROP TABLE IF EXISTS produto");
        db.execSQL("DROP TABLE IF EXISTS loja");
        db.execSQL("DROP TABLE IF EXISTS bebida");
        db.execSQL("DROP TABLE IF EXISTS modelo");
        db.execSQL("DROP TABLE IF EXISTS marca");
        onCreate(db);
    }

    // DB info
    private static final String NOME_BASE_DE_DADOS = "MYBEERAPP.db";
    private static final int VERSAO_BASE_DE_DADOS = 3;

    // create tables
    private static final String CREATE_MARCA_TABLE = "CREATE TABLE marca (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL)";

    private static final String CREATE_MODELO_TABLE = "CREATE TABLE modelo (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, volume INTEGER NOT NULL)";

    private static final String CREATE_LOJA_TABLE = "CREATE TABLE loja (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL)";

    private static final String CREATE_BEBIDA_TABLE = "CREATE TABLE bebida (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, BLOB foto, marcaId INTEGER NOT NULL, " +
            "modeloId INTEGER NOT NULL, FOREIGN KEY(marcaId) REFERENCES marca(id)," +
            "FOREIGN KEY(modeloId) REFERENCES modelo(id))";

    private static final String CREATE_PRODUTO_TABLE = "CREATE TABLE produto (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, lojaId INTEGER NOT NULL, " +
            "bebidaId INTEGER NOT NULL, precoUnidade REAL(8,2) NOT NULL, " +
            "precoML REAL(8,2) NOT NULL, ultimaAtualizacao TEXT, " +
            "FOREIGN KEY(lojaId) REFERENCES loja(id)," +
            "FOREIGN KEY(bebidaId) REFERENCES bebida(id))";

    private static final String CREATE_CESTA_TABLE = "CREATE TABLE cesta (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT NOT NULL, " +
            "valorTotal REAL(8,2))";

    private static final String CREATE_PRODUTOS_CESTAS_TABLE = "CREATE TABLE produtos_cestas (" +
            "produtoId INTEGER NOT NULL, cestaId INTEGER NOT NULL, " +
            "qtdeProdutos INTEGER NOT NULL," +
            "FOREIGN KEY(produtoId) REFERENCES produto(id)," +
            "FOREIGN KEY(cestaId) REFERENCES cesta(id))";

}