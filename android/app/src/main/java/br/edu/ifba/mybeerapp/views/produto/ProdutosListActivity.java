package br.edu.ifba.mybeerapp.views.produto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;

public class ProdutosListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadProdutosList();

        final ProdutosListActivity activity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_produto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CadastroProdutoActivity.class);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 200) {
                boolean success = data.getBooleanExtra("success", false);
                if (success)
                    this.loadProdutosList();
            }
        }
    }

    private void loadProdutosList() {
        final ListView theListView = findViewById(R.id.produtosListView);

        IProdutoRepository produtoRepository = RepositoryLoader.getInstance().getProdutoRepository();

        // callback para carregar lista de produtos
        final ProdutosListActivity activity = this;
        produtoRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                final ProdutosListView produtosAdapter = new ProdutosListView(activity, models);
                theListView.setAdapter(produtosAdapter);
                produtosAdapter.notifyDataSetChanged();
            }

            @Override
            public void success(IModel models) { }

            @Override
            public void fail(String message) {
                Toast.makeText(getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });

        produtoRepository.retrieveAll();
    }
}
