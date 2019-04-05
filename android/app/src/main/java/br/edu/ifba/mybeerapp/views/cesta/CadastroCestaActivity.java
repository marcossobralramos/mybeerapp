package br.edu.ifba.mybeerapp.views.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;

public class CadastroCestaActivity extends AppCompatActivity {
    private Cesta cesta;
    private ICestaRepository cestaRepository;
    private CadastroCestaView cestaView;

    public CadastroCestaView getCestaView() {
        return cestaView;
    }

    public void setCestaView(CadastroCestaView cestaView) {
        this.cestaView = cestaView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cestaRepository = RepositoryLoader.getInstance().getCestaRepository(getApplicationContext());
        this.loadProdutosCesta();

        final int idCesta = getIntent().getIntExtra("cestaId", 0);
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

    protected void loadProdutosCesta() {
        final int idCesta = getIntent().getIntExtra("cestaId", 0);
        final CadastroCestaActivity activity = this;
        this.cestaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {

            }

            @Override
            public void success(IModel model) {
                activity.cesta = (Cesta) model;
                ListView theListView = findViewById(R.id.cadastroCestaView);
                activity.cestaView = new CadastroCestaView(activity, activity.cesta.getProdutos());
                theListView.setAdapter(activity.cestaView);
                activity.cestaView.notifyDataSetChanged();
            }

            @Override
            public void fail(String message) {

            }
        });
        this.cestaRepository.retrieveById(idCesta);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 200) {
                boolean success = data.getBooleanExtra("success", false);
                if (success)
                    this.loadProdutosCesta();
            }
        }
    }

}