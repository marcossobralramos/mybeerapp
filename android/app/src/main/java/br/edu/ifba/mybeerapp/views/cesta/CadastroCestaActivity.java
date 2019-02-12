package br.edu.ifba.mybeerapp.views.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import br.edu.ifba.mybeerapp.views.produto.CadastroProdutoActivity;
import br.edu.ifba.mybeerapp.views.produto.ProdutosListActivity;

public class CadastroCestaActivity extends AppCompatActivity {
    private Cesta cesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final int idCesta = getIntent().getIntExtra("idCesta", 0);

        try {
            this.cesta = (Cesta) (new CestaRepository(this.getApplicationContext())).retrieveById(idCesta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            loadCesta();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        final CadastroCestaActivity activity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_produto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CadastroProdutoCestaActivity.class);
                intent.putExtra("idCesta", idCesta);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    protected void loadCesta() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException
    {
        ListView theListView = findViewById(R.id.cadastroCestaView);
        this.cesta = (Cesta) (new CestaRepository(getApplicationContext())).retrieveById(this.cesta.getId());
        final CadastroCestaView adapter = new CadastroCestaView(this, this.cesta.getProdutos());
        theListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 200) {
                boolean success = data.getBooleanExtra("success", false);
                if (success) {
                    try {
                        this.loadCesta();
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                            | InvocationTargetException | IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}