package br.edu.ifba.mybeerapp.views.produto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;

public class ProdutosListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            loadProdutosList();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) {
            if(resultCode == 200){
                boolean resposta = data.getBooleanExtra("success", false);
                if(resposta == true) {
                    try {
                        this.loadProdutosList();
                    } catch (ClassNotFoundException | NoSuchMethodException |
                            InvocationTargetException | IllegalAccessException |
                            InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadProdutosList() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException
    {
        ListView theListView = findViewById(R.id.produtosListView);

        ArrayList<IModel> produtosList = null;

        produtosList = (ArrayList<IModel>) (new ProdutoRepository(this.getApplicationContext())).retrieveAll();

        final ProdutosListView adapter = new ProdutosListView(this, produtosList);

        // set elements to adapter
        theListView.setAdapter(adapter);
    }
}
