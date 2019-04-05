package br.edu.ifba.mybeerapp.views.loja;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.LojaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.Repository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;

public class LojasListActivity extends AppCompatActivity {

    private ListView lojasListView;

    private ILojaRepository lojaRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lojas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.lojasListView = findViewById(R.id.lojasListView);
        this.lojaRepository = RepositoryLoader.getInstance().getLojaRepository(getApplicationContext());

        loadLojasList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_loja);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogCadastroLoja();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLojasList();
    }

    protected void loadDialogCadastroLoja() {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.form_cadastro_loja);
        myDialog.setCancelable(true);

        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);

        Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);

        myDialog.show();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loja loja = new Loja(descricao.getText().toString());
                lojaRepository.setViewCallback(new ViewCallback() {
                    @Override
                    public void success(ArrayList<IModel> models) {

                    }

                    @Override
                    public void success(IModel model) {
                        Toast.makeText(getApplicationContext(), "Loja salva com sucesso!"
                                , Toast.LENGTH_SHORT);
                        LojasListView adapter = (LojasListView) lojasListView.getAdapter();
                        adapter.add(model);
                    }

                    @Override
                    public void fail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    }
                });
                lojaRepository.create(loja);
            }
        });
    }

    protected void loadLojasList() {
        final LojasListActivity activity = this;
        lojaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                final LojasListView adapter = new LojasListView(activity, models);
                lojasListView.setAdapter(adapter);
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
        lojaRepository.retrieveAll();
    }

    public ILojaRepository getLojaRepository() {
        return lojaRepository;
    }

    public ListView getLojasListView() {
        return lojasListView;
    }
}