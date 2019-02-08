package br.edu.ifba.mybeerapp.views.cesta;

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
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;

public class CadastroCestaActivity extends AppCompatActivity
{
    private Cesta cesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        int idCesta = getIntent().getIntExtra("idCesta", 0);

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

        loadCesta();
    }

    protected void loadCesta()
    {
        ListView theListView = findViewById(R.id.cadastroCestaView);
        final CadastroCestaView adapter = new CadastroCestaView(this, this.cesta.getProdutos());
        theListView.setAdapter(adapter);
    }

}