package br.edu.ifba.mybeerapp.views.cesta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;

public class ListCestasActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cestas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onResume()
    {
        super.onResume();
        loadCestaList();
    }

    protected void loadDialogCadastroCesta()
    {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.form_cadastro_cesta);
        myDialog.setCancelable(true);

        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);

        Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);
        Button btnVerProdutos = (Button) myDialog.findViewById(R.id.ver_produtos);
        btnVerProdutos.setEnabled(false);
        myDialog.show();

        final Context context = this;
        btnSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Cesta cesta = new Cesta();
                cesta.setDescricao(descricao.getText().toString());
                try {
                    if((new CestaRepository(context).create(cesta) != 1))
                    {
                        Toast.makeText(context, "Cesta salva com sucesso!"
                                , Toast.LENGTH_SHORT);
                    }
                            else
                        {
                            Toast.makeText(context, "Erro ao salvar a cesta!"
                                    , Toast.LENGTH_SHORT);
                        }
                } catch (IllegalAccessException | InvocationTargetException | IOException
                        | NoSuchMethodException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                Activity activity = (Activity) context;
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        });
    }

    protected void loadCestaList()
    {
        ListView theListView = findViewById(R.id.cestaListView);

        ArrayList<IModel> cestaList = null;

        try {
            cestaList = (ArrayList<IModel>) (new CestaRepository(this.getApplicationContext())).retrieveAll();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        final ListCestaView adapter = new ListCestaView(this, cestaList);

        // set elements to adapter
        theListView.setAdapter(adapter);
    }
}
