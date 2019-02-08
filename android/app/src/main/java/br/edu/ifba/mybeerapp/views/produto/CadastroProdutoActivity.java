package br.edu.ifba.mybeerapp.views.produto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.List;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;
import br.edu.ifba.mybeerapp.repository.LojaRepository;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;

public class CadastroProdutoActivity extends AppCompatActivity
{
    private static Produto thisProduto;

    public CadastroProdutoActivity() {
        super();
        this.thisProduto = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cadastro_produto);

        this.loadComboboxes();

        int idProduto = getIntent().getIntExtra("idProduto", 0);

        // then edit
        if (idProduto != 0)
            this.loadProduto(idProduto);

        Button btnSalvar = findViewById(R.id.salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Spinner comboLojas = findViewById(R.id.combo_lojas);
                Loja loja = (Loja) comboLojas.getSelectedItem();

                Spinner comboBebidas = findViewById(R.id.combo_bebidas);
                Bebida bebida = (Bebida) comboBebidas.getSelectedItem();

                EditText preco = findViewById(R.id.preco);

                Produto produto = new Produto();
                produto.setBebida(bebida);
                produto.setLoja(loja);
                produto.setPrecoUnidade(Double.parseDouble(preco.getText().toString()));
                produto.setUltimaAtualizacao(new Date(System.currentTimeMillis()).toString());

                try
                {
                    long result = -1;
                    if(thisProduto == null) {
                        result = (new ProdutoRepository(getApplicationContext())).create(produto);
                    } else {
                        result = (new ProdutoRepository(getApplicationContext())).update(thisProduto, produto);
                    }

                    if (result == -1)
                        Toast.makeText(getApplicationContext(),
                                "Falha ao salvar o produto!",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Produto salvo com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("success",true);
                        setResult(200, returnIntent);
                        finish();
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
                        | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadComboboxes()
    {
        this.loadLojasCombobox();
        this.loadBebidasCombobox();
    }

    private void loadBebidasCombobox()
    {
        List<IModel> bebidas = null;
        try {
            bebidas = (new BebidaRepository(this.getApplicationContext())).retrieveAll();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Spinner comboBebidas = (Spinner) findViewById(R.id.combo_bebidas);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, bebidas);

        comboBebidas.setAdapter(adapter);
    }

    private void loadLojasCombobox()
    {
        List<IModel> lojas = null;
        try {
            lojas = (new LojaRepository(this.getApplicationContext())).retrieveAll();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Spinner comboLojas = (Spinner) findViewById(R.id.combo_lojas);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, lojas);
        comboLojas.setAdapter(adapter);
    }

    private void loadProduto(int idProduto)
    {
        try {
            thisProduto = (Produto) (new ProdutoRepository(this.getApplicationContext())).retrieveById(idProduto);
            Spinner comboLojas = findViewById(R.id.combo_lojas);

            for(int x = 0; x < comboLojas.getAdapter().getCount(); x++)
            {
                if(comboLojas.getAdapter().getItem(x).equals(thisProduto.getLoja()))
                {
                    comboLojas.setSelection(x,true);
                    break;
                }
            }

            Spinner comboBebidas = findViewById(R.id.combo_bebidas);

            for(int x = 0; x < comboBebidas.getAdapter().getCount(); x++)
            {
                if(comboBebidas.getAdapter().getItem(x).equals(thisProduto.getBebida()))
                {
                    comboBebidas.setSelection(x,true);
                    break;
                }
            }

            EditText preco = findViewById(R.id.preco);
            preco.setText(String.valueOf(thisProduto.getPrecoUnidade()));

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
                | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}