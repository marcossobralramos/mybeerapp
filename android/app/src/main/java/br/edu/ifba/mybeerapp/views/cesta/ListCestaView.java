package br.edu.ifba.mybeerapp.views.cesta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import br.edu.ifba.mybeerapp.MainActivity;
import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import foldingcell.FoldingCell;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ListCestaView extends ArrayAdapter<IModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Activity activity;

    public ListCestaView(Context context, ArrayList<IModel> objects) {
        super(context, 0, objects);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Cesta cesta = (Cesta) getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.content_list_cesta, parent, false);
            // binding view parts to view holder
            viewHolder.descricao = cell.findViewById(R.id.descricao);
            viewHolder.litros = cell.findViewById(R.id.litros);
            viewHolder.precoTotal = cell.findViewById(R.id.preco_total);
            viewHolder.edit = cell.findViewById(R.id.edit_cesta);
            viewHolder.remove = cell.findViewById(R.id.remove_cesta);
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

        if (null == cesta)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.descricao.setText(cesta.getDescricao());
        viewHolder.litros.setText(String.format("%.3f", cesta.getTotalLitros()) + "L");
        viewHolder.precoTotal.setText("R$" + String.format("%.2f", cesta.getValorTotal()));
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CadastroCestaActivity.class);
                intent.putExtra("idCesta", cesta.getId());
                getContext().startActivity(intent);
            }
        });
        final FoldingCell finalCell = cell;
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new CestaRepository(getContext())).delete(cesta.getId());
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
        TextView descricao;
        TextView litros;
        TextView precoTotal;
        LinearLayout edit;
        Button remove;
    }
}