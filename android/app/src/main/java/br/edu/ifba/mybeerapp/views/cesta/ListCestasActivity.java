package br.edu.ifba.mybeerapp.views.cesta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;

public class ListCestasActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cestas);

        loadCestaList();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_cesta);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Abrir activity para cadastrar uma cesta.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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
