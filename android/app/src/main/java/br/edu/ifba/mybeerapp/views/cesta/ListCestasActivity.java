package br.edu.ifba.mybeerapp.views.cesta;

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

import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.Repository;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;

public class ListCestasActivity extends AppCompatActivity {

    private ListView cestasListView;
    private ICestaRepository cestaRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cestas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cestaRepository = RepositoryLoader.getInstance().getCestaRepository(getApplicationContext());

        loadCestaList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_cesta);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogCadastroCesta();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCestaList();
    }

    protected void loadDialogCadastroCesta() {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.form_cadastro_cesta);
        myDialog.setCancelable(true);

        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);

        Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);
        Button btnVerProdutos = (Button) myDialog.findViewById(R.id.ver_produtos);
        btnVerProdutos.setEnabled(false);
        myDialog.show();

        final ListCestasActivity activity = this;
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cesta cesta = new Cesta();
                cesta.setDescricao(descricao.getText().toString());
                cesta.setTotalLitros(0);
                cesta.setValorTotal(0);

                ICestaRepository cestaRepository = RepositoryLoader.getInstance().getCestaRepository(getApplicationContext());
                cestaRepository.setViewCallback(new ViewCallback() {
                    @Override
                    public void success(ArrayList<IModel> models) {

                    }

                    @Override
                    public void success(IModel model) {
                        ListCestaView adapter = (ListCestaView) cestasListView.getAdapter();
                        adapter.add(model);
                        myDialog.hide();

                        Toast.makeText(getApplicationContext(), "Cesta salva com sucesso!"
                                , Toast.LENGTH_LONG);
                    }

                    @Override
                    public void fail(String message) {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG);
                    }
                });
                cestaRepository.create(cesta);
            }
        });
    }

    protected void loadCestaList() {
        final ListCestasActivity activity = this;
        cestaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                activity.cestasListView = findViewById(R.id.cestaListView);
                ListCestaView adapter = new ListCestaView(activity, models);
                cestasListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void success(IModel model) {

            }

            @Override
            public void fail(String message) {
                Toast.makeText(activity, message, Toast.LENGTH_LONG);
            }
        });
        this.cestaRepository.retrieveAll();
    }

    public ListView getCestasListView() {
        return cestasListView;
    }

    public void setCestasListView(ListView cestasListView) {
        this.cestasListView = cestasListView;
    }
}
