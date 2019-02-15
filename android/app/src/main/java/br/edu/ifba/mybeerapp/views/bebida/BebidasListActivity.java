package br.edu.ifba.mybeerapp.views.bebida;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;
public class BebidasListActivity extends AppCompatActivity
{
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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadBebidasList();
    }

    protected void loadDialogCadastroBebida()
    {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.form_cadastro_bebida);
        myDialog.setCancelable(true);

        Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);

        myDialog.show();
    }

    protected void loadBebidasList()
    {
        ListView theListView = findViewById(R.id.lojasListView);

        ArrayList<IModel> bebidasList = null;

        try {
            bebidasList = (ArrayList<IModel>) (new BebidaRepository(this.getApplicationContext())).retrieveAll();
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

        final BebidasListView adapter = new BebidasListView(this, bebidasList);

        // set elements to adapter
        theListView.setAdapter(adapter);
    }
}