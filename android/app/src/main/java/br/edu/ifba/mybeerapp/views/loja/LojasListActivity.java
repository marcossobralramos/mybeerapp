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
import br.edu.ifba.mybeerapp.repository.LojaRepository;

public class LojasListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lojas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onResume()
    {
        super.onResume();
        loadLojasList();
    }

    protected void loadDialogCadastroLoja()
    {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.form_cadastro_loja);
        myDialog.setCancelable(true);

        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);

        Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);

        myDialog.show();

        final Context context = this;
        btnSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Loja loja = new Loja(descricao.getText().toString());
                try {
                    if((new LojaRepository(context).create(loja) != 1))
                    {
                        Toast.makeText(context, "Loja salva com sucesso!"
                                , Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        Toast.makeText(context, "Erro ao salvar a loja!"
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

    protected void loadLojasList()
    {
        ListView theListView = findViewById(R.id.lojasListView);

        ArrayList<IModel> lojasList = null;

        try {
            lojasList = (ArrayList<IModel>) (new LojaRepository(this.getApplicationContext())).retrieveAll();
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

        final LojasListView adapter = new LojasListView(this, lojasList);

        // set elements to adapter
        theListView.setAdapter(adapter);
    }
}