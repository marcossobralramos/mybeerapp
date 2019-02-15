package br.edu.ifba.mybeerapp.views.loja;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import br.edu.ifba.mybeerapp.repository.LojaRepository;
import foldingcell.FoldingCell;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LojasListView extends ArrayAdapter<IModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public LojasListView(Context context, ArrayList<IModel> objects) {
        super(context, 0, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Loja loja = (Loja) getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.content_lojas_list, parent, false);
            // binding view parts to view holder
            viewHolder.loja = cell.findViewById(R.id.loja);
            viewHolder.edit = cell.findViewById(R.id.edit_loja);
            viewHolder.remove = cell.findViewById(R.id.remove_loja);
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

        if (null == loja)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.loja.setText(loja.getNome());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.form_cadastro_loja);
                myDialog.setCancelable(true);

                final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                descricao.setText(loja.getNome());

                Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);

                myDialog.show();

                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                        Loja newLoja = new Loja();
                        newLoja.setNome(descricao.getText().toString());
                        try {
                            if ((new LojaRepository(getContext())).update(loja, newLoja) != 1) {
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
                });

            }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new LojaRepository(getContext()).delete(loja.getId()) != -1)
                {
                    Toast.makeText(getContext(), "Loja removida com sucesso!"
                            , Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(getContext(), "Erro ao remover a loja!"
                            , Toast.LENGTH_SHORT);
                }
                Activity activity = (Activity) getContext();
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        });

        final FoldingCell finalCell = cell;
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new LojaRepository(getContext())).delete(loja.getId());
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
        TextView loja;
        LinearLayout edit;
        Button remove;
    }
}