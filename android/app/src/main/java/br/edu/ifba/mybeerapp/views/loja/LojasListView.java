package br.edu.ifba.mybeerapp.views.loja;

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
import java.util.Comparator;
import java.util.HashSet;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.Repository;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.LojaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;
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
                final LojasListActivity activity = (LojasListActivity) getContext();
                final Dialog myDialog = new Dialog(activity);
                myDialog.setContentView(R.layout.form_cadastro_loja);
                myDialog.setCancelable(true);

                final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                descricao.setText(loja.getNome());

                myDialog.show();

                Button btnSalvar = (Button) myDialog.findViewById(R.id.salvar);
                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText descricao = (EditText) myDialog.findViewById(R.id.descricao);
                        Loja newLoja = new Loja();
                        newLoja.setNome(descricao.getText().toString());

                        ILojaRepository lojaRepository = activity.getLojaRepository();
                        lojaRepository.setViewCallback(new ViewCallback() {
                            @Override
                            public void success(ArrayList<IModel> models) {

                            }

                            @Override
                            public void success(IModel model) {
                                myDialog.hide();
                                LojasListView adapter = (LojasListView) activity.getLojasListView().getAdapter();
                                adapter.remove(model);
                                adapter.add(model);
                                Toast.makeText(getContext(), "Loja alterada com sucesso!"
                                        , Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void fail(String message) {
                                Toast.makeText(activity, message, Toast.LENGTH_LONG);
                            }
                        });
                        lojaRepository.update(loja, newLoja);
                    }
                });
            }
        });

        final FoldingCell finalCell = cell;
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ILojaRepository lojaRepository = RepositoryLoader.getInstance().getLojaRepository(getContext());
                lojaRepository.setViewCallback(new ViewCallback() {
                    @Override
                    public void success(ArrayList<IModel> models) {

                    }

                    @Override
                    public void success(IModel model) {
                        Activity activity = (Activity) getContext();
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                    }

                    @Override
                    public void fail(String message) {

                    }
                });
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