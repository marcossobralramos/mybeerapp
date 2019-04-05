package br.edu.ifba.mybeerapp.views.cesta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.Repository;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;
import br.edu.ifba.mybeerapp.views.loja.LojasListActivity;
import br.edu.ifba.mybeerapp.views.produto.CadastroProdutoActivity;
import br.edu.ifba.mybeerapp.views.produto.ProdutosListActivity;

public class CadastroProdutoCestaActivity extends AppCompatActivity {
    private static Cesta thisCesta;
    private ICestaRepository cestaRepository;
    private final CadastroProdutoCestaActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cadastro_produto_cesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cestaRepository = RepositoryLoader.getInstance().getCestaRepository(getApplicationContext());

        final int idProduto = getIntent().getIntExtra("idProduto", 0);
        final int idCesta = getIntent().getIntExtra("idCesta", 0);

        cestaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {

            }

            @Override
            public void success(IModel model) {
                thisCesta = (Cesta) model;
            }

            @Override
            public void fail(String message) {

            }
        });
        cestaRepository.retrieveById(idCesta);

        loadLojasCombobox(idProduto, idCesta);
    }

    private void loadLojasCombobox(final int idProduto, final int idCesta) {
        ILojaRepository lojaRepository = RepositoryLoader.getInstance().getLojaRepository(getApplicationContext());
        lojaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
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

                ArrayAdapter adapter = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, models);
                comboLojas.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void success(IModel model) {

            }

            @Override
            public void fail(String message) {

            }
        });
        lojaRepository.retrieveAll();
    }

    private void loadProdutosCombo(int lojaId, final int idProduto) {
        IProdutoRepository produtoRepository = RepositoryLoader.getInstance().getProdutoRepository(getApplicationContext());
        produtoRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                Spinner comboProdutos = (Spinner) findViewById(R.id.combo_produtos);
                comboProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Produto produto = (Produto) parent.getItemAtPosition(position);
                        if(!thisCesta.produtoExiste(produto)) {
                            TextView preco = findViewById(R.id.preco);
                            preco.setText(String.valueOf(produto.getPrecoUnidade()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapter = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, models);
                comboProdutos.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadButtons(idProduto);
                // then edit
                if (idProduto != 0)
                    loadProduto(idProduto);
            }

            @Override
            public void success(IModel model) {

            }

            @Override
            public void fail(String message) {

            }
        });
        produtoRepository.retrieveByLoja(lojaId);
    }

    private void loadButtons(final int idProduto) {
        final CadastroProdutoCestaActivity activity = this;
        Button abrirLojas = findViewById(R.id.abrir_lojas);
        abrirLojas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LojasListActivity.class);
                activity.startActivity(intent);
            }
        });

        Button abrirProdutos = findViewById(R.id.abrir_produtos);
        abrirProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProdutosListActivity.class);
                startActivity(intent);
            }
        });

        Button btnSalvarProdutos = findViewById(R.id.salvar_produtos);

        btnSalvarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // callback para modificações na cesta
                cestaRepository.setViewCallback(new ViewCallback() {
                    @Override
                    public void success(ArrayList<IModel> models) {

                    }

                    @Override
                    public void success(IModel model) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("success", true);
                        setResult(200, returnIntent);
                        finish();
                    }

                    @Override
                    public void fail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

                IProdutoRepository produtoRepository = RepositoryLoader.getInstance().getProdutoRepository(getApplicationContext());

                Produto produto = activity.thisCesta.getProduto(idProduto);

                if(produto == null) {
                    int id = (idProduto == 0) ?
                            ((Produto) ((Spinner) findViewById(R.id.combo_produtos))
                                    .getSelectedItem()).getId()
                            : idProduto;

                    produtoRepository.setViewCallback(new ViewCallback() {
                        @Override
                        public void success(ArrayList<IModel> models) {

                        }

                        @Override
                        public void success(IModel model) {
                            Produto produto = (Produto) model;
                            EditText precoET = findViewById(R.id.preco);
                            double preco = Double.parseDouble(precoET.getText().toString());
                            EditText qtdeET = findViewById(R.id.qtde);
                            int qtde = Integer.parseInt(qtdeET.getText().toString());
                            produto.setPrecoUnidade(preco);
                            produto.setQtde(qtde);
                            produto.setCestaId(thisCesta.getId());

                            cestaRepository.addProduto(produto);
                        }

                        @Override
                        public void fail(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    produtoRepository.retrieveById(id);
                } else {
                    EditText precoET = findViewById(R.id.preco);
                    double preco = Double.parseDouble(precoET.getText().toString());
                    EditText qtdeET = findViewById(R.id.qtde);
                    int qtde = Integer.parseInt(qtdeET.getText().toString());
                    produto.setPrecoUnidade(preco);
                    produto.setQtde(qtde);
                    produto.setCestaId(thisCesta.getId());
                    cestaRepository.updateProduto(produto);
                }

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadProduto(final int idProduto) {
        Produto produto = thisCesta.getProduto(idProduto);

        Spinner comboLojas = findViewById(R.id.combo_lojas);
        for (int x = 0; x < comboLojas.getAdapter().getCount(); x++) {
            if (comboLojas.getAdapter().getItem(x).equals(produto.getLoja())) {
                comboLojas.setSelection(x, true);
                break;
            }
        }

        Spinner comboProdutos = findViewById(R.id.combo_produtos);
        for (int x = 0; x < comboProdutos.getAdapter().getCount(); x++) {
            if (comboProdutos.getAdapter().getItem(x).equals(produto)) {
                comboProdutos.setSelection(x, true);
                break;
            }
        }

        EditText preco = findViewById(R.id.preco);
        preco.setText(String.valueOf(produto.getPrecoUnidade()));

        EditText qtde = findViewById(R.id.qtde);
        qtde.setText(String.valueOf(produto.getQtde()));
    }

}