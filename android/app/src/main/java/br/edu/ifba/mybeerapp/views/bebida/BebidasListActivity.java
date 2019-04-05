package br.edu.ifba.mybeerapp.views.bebida;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.BebidaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;

public class BebidasListActivity extends AppCompatActivity
{
    private Dialog formCadastroBebida;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bebidas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadBebidasList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_loja);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogCadastroBebida();
            }
        });

        this.formCadastroBebida = new Dialog(this);
        this.formCadastroBebida.setContentView(R.layout.form_cadastro_bebida);

        Button btnSalvar = (Button) formCadastroBebida.findViewById(R.id.salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadBebidasList();
    }

    protected void loadDialogCadastroBebida()
    {
        this.formCadastroBebida.setCancelable(true);
        this.formCadastroBebida.show();
    }

    protected void loadBebidasList()
    {
        final ListView theListView = findViewById(R.id.lojasListView);

        final BebidasListActivity activity = this;

        IBebidaRepository bebidaRepository = RepositoryLoader.getInstance().getBebidaRepository(getApplicationContext());
        bebidaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                final BebidasListView adapter = new BebidasListView(activity, models);
                theListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void success(IModel model) {

            }

            @Override
            public void fail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            }
        });
        bebidaRepository.retrieveAll();
    }
}