package br.edu.ifba.mybeerapp.views.cesta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.List;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import br.edu.ifba.mybeerapp.repository.LojaRepository;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;

public class CadastroProdutoCestaActivity extends AppCompatActivity {
    private static Cesta thisCesta;

    public CadastroProdutoCestaActivity() {
        super();
        this.thisCesta = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cadastro_produto_cesta);

        final int idProduto = getIntent().getIntExtra("idProduto", 0);
        final int idCesta = getIntent().getIntExtra("idCesta", 0);

        try {
            thisCesta = (Cesta) (new CestaRepository(this.getApplicationContext())).retrieveById(idCesta);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException
                | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadLojasCombobox(idProduto);

        // then edit
        if (idCesta != 0 && idProduto != 0)
            this.loadProduto(idProduto, idCesta);

    }

    private void loadLojasCombobox(final int idProduto) {
        List<IModel> lojas = null;
        try {
            lojas = (new LojaRepository(this.getApplicationContext())).retrieveAll();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Spinner comboLojas = (Spinner) findViewById(R.id.combo_lojas);
        comboLojas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Loja loja = (Loja) parent.getItemAtPosition(position);
                loadProdutosCombo(loja.getId(), idProduto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, lojas);
        comboLojas.setAdapter(adapter);
    }

    private void loadProdutosCombo(int lojaId, final int idProduto) {
        List<IModel> produtos = null;
        try {
            produtos = (new ProdutoRepository(this.getApplicationContext())).retrieveByLoja(lojaId);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Spinner comboProdutos = (Spinner) findViewById(R.id.combo_produtos);
        comboProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Produto produto = (Produto) parent.getItemAtPosition(position);
                TextView preco = findViewById(R.id.preco);
                preco.setText(String.valueOf(produto.getPrecoUnidade()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, produtos);
        comboProdutos.setAdapter(adapter);
        loadButtons(idProduto);
    }

    private void loadButtons(final int idProduto) {
        Button btnSalvarProdutos = findViewById(R.id.salvar_produtos);

        btnSalvarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produto produto = null;

                try {
                    int id = (idProduto == 0) ?
                            ((Produto) ((Spinner) findViewById(R.id.combo_produtos))
                                    .getSelectedItem()).getId()
                            : idProduto;
                    produto = (Produto) (new ProdutoRepository(getApplicationContext())).retrieveById(id);
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException |
                        InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

                EditText precoET = findViewById(R.id.preco);
                double preco = Double.parseDouble(precoET.getText().toString());
                EditText qtdeET = findViewById(R.id.qtde);
                int qtde = Integer.parseInt(qtdeET.getText().toString());
                produto.setPrecoUnidade(preco);
                produto.setQtde(qtde);

                long result = -1;
                if (thisCesta != null) {
                    try {
                        result = (new CestaRepository(getApplicationContext())).addProduto(produto, thisCesta.getId());
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                            | InvocationTargetException | IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
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
                    returnIntent.putExtra("success", true);
                    setResult(200, returnIntent);
                    finish();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadProduto(int idProduto, int idCesta) {
        Spinner comboLojas = findViewById(R.id.combo_lojas);
        for (int x = 0; x < comboLojas.getAdapter().getCount(); x++) {
            if (comboLojas.getAdapter().getItem(x).equals(thisCesta.getProduto(idProduto).getLoja())) {
                comboLojas.setSelection(x, true);
                break;
            }
        }

        Spinner comboProdutos = findViewById(R.id.combo_produtos);

        for (int x = 0; x < comboProdutos.getAdapter().getCount(); x++) {
            if (comboProdutos.getAdapter().getItem(x).equals(thisCesta.getProduto(idProduto))) {
                comboProdutos.setSelection(x, true);
                break;
            }
        }

        EditText preco = findViewById(R.id.preco);
        preco.setText(String.valueOf(thisCesta.getProduto(idProduto).getPrecoUnidade()));

        EditText qtde = findViewById(R.id.qtde);
        qtde.setText(String.valueOf(thisCesta.getProduto(idProduto).getQtde()));
    }

}