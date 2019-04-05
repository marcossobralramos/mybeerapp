package br.edu.ifba.mybeerapp.views.produto;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;
import br.edu.ifba.mybeerapp.views.bebida.BebidasListActivity;
import br.edu.ifba.mybeerapp.views.cesta.CadastroCestaActivity;
import br.edu.ifba.mybeerapp.views.loja.LojasListActivity;

public class CadastroProdutoActivity extends AppCompatActivity {
    private static Produto thisProduto;
    final CadastroProdutoActivity activity = this;

    public CadastroProdutoActivity() {
        super();
        this.thisProduto = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cadastro_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.loadLojasCombobox();

        Button btnSalvar = findViewById(R.id.salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                RepositoryLoader loader = RepositoryLoader.getInstance();

                IProdutoRepository produtoRepository = RepositoryLoader.getInstance()
                        .getProdutoRepository(getApplicationContext());

                produtoRepository.setViewCallback(new ViewCallback() {
                    @Override
                    public void success(ArrayList<IModel> models) {

                    }

                    @Override
                    public void success(IModel model) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("success", (model != null));
                        setResult(200, returnIntent);
                        finish();
                    }

                    @Override
                    public void fail(String message) {
                        Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                if (thisProduto == null)
                    produtoRepository.create(produto);
                else
                    produtoRepository.update(thisProduto, produto);
            }
        });

        this.loadButtons();

    }

    private void loadBebidasCombobox() {
        IBebidaRepository bebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();
        // callback para carregar lista de lojas
        bebidaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                Spinner comboBebidas = (Spinner) findViewById(R.id.combo_bebidas);

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_item, models);

                comboBebidas.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                // carregar produto
                int idProduto = activity.getIntent().getIntExtra("idProduto", 0);
                if (idProduto != 0)
                    activity.loadProduto(idProduto);
            }

            @Override
            public void success(IModel model) { }

            @Override
            public void fail(String message) {
                Toast.makeText(getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });
        bebidaRepository.retrieveAll();
    }

    private void loadLojasCombobox() {
        ILojaRepository lojaRepository = RepositoryLoader.getInstance().getLojaRepository();
        // callback para carregar lista de lojas
        lojaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                Spinner comboLojas = (Spinner) findViewById(R.id.combo_lojas);

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_item, models);

                comboLojas.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                activity.loadBebidasCombobox();
            }

            @Override
            public void success(IModel model) { }

            @Override
            public void fail(String message) {
                Toast.makeText(getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });
        lojaRepository.retrieveAll();
    }

    private void loadButtons() {
        final CadastroProdutoActivity activity = this;
        Button abrirLojas = findViewById(R.id.abrir_lojas);
        abrirLojas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LojasListActivity.class);
                activity.startActivity(intent);
            }
        });

        Button abrirBebidas = findViewById(R.id.abrir_bebidas);
        abrirBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BebidasListActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    private void loadProduto(int idProduto) {
        IProdutoRepository produtoRepository = RepositoryLoader.getInstance().getProdutoRepository();
        // callback para carregar infos do produto
        produtoRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) { }

            @Override
            public void success(IModel model) {
                thisProduto = (Produto) model;

                final Spinner comboLojas = findViewById(R.id.combo_lojas);
                comboLojas.getAdapter().registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        for (int x = 0; x < comboLojas.getAdapter().getCount(); x++) {
                            if (comboLojas.getAdapter().getItem(x).equals(thisProduto.getLoja())) {
                                comboLojas.setSelection(x, true);
                                break;
                            }
                        }
                    }
                });

                final Spinner comboBebidas = findViewById(R.id.combo_bebidas);
                comboBebidas.getAdapter().registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        for (int x = 0; x < comboBebidas.getAdapter().getCount(); x++) {
                            if (comboBebidas.getAdapter().getItem(x).equals(thisProduto.getBebida())) {
                                comboBebidas.setSelection(x, true);
                                break;
                            }
                        }
                    }
                });

                EditText preco = findViewById(R.id.preco);
                preco.setText(String.valueOf(thisProduto.getPrecoUnidade()));
            }

            @Override
            public void fail(String message) {
                Toast.makeText(getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });
        produtoRepository.retrieveById(idProduto);
    }

}