package br.edu.ifba.mybeerapp.views.bebida;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;
import foldingcell.FoldingCell;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BebidasListView extends ArrayAdapter<IModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public BebidasListView(Context context, ArrayList<IModel> objects) {
        super(context, 0, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Bebida bebida = (Bebida) getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.content_bebidas_list, parent, false);
            // binding view parts to view holder
            viewHolder.marca = cell.findViewById(R.id.marca);
            viewHolder.modelo = cell.findViewById(R.id.modelo);
            viewHolder.edit = cell.findViewById(R.id.edit_bebida);
            viewHolder.remove = cell.findViewById(R.id.remove_bebida);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == bebida)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.marca.setText(bebida.getMarca().getNome());
        viewHolder.modelo.setText(bebida.getModelo().getNome());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.form_cadastro_bebida);
                myDialog.setCancelable(true);

                /*final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                descricao.setText(bebida.getNome());

                Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);

                myDialog.show();

                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                        bebida newbebida = new bebida();
                        newbebida.setNome(descricao.getText().toString());
                        try {
                            if ((new bebidaRepository(getContext())).update(bebida, newbebida) != 1) {
                                Toast.makeText(getContext(), "Cesta salva com sucesso!"
                                        , Toast.LENGTH_SHORT);
                            } else {
                                Toast.makeText(getContext(), "Erro ao salvar a cesta!"
                                        , Toast.LENGTH_SHORT);
                            }
                        } catch (IllegalAccessException | InvocationTargetException | IOException
                                | NoSuchMethodException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Activity activity = (Activity) getContext();
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                    }
                });*/

            }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new BebidaRepository(getContext()).delete(bebida.getId()) != -1)
                {
                    Toast.makeText(getContext(), "bebida removida com sucesso!"
                            , Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(getContext(), "Erro ao remover a bebida!"
                            , Toast.LENGTH_SHORT);
                }
                Activity activity = (Activity) getContext();
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        });

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView marca;
        TextView modelo;
        LinearLayout edit;
        Button remove;
    }
}