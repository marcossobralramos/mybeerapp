package br.edu.ifba.mybeerapp.views.produto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;
import br.edu.ifba.mybeerapp.views.cesta.CadastroCestaActivity;
import foldingcell.FoldingCell;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ProdutosListView extends ArrayAdapter<IModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public ProdutosListView(Context context, ArrayList<IModel> objects) {
        super(context, 0, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Produto produto = (Produto) getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.content_produtos_list, parent, false);
            // binding view parts to view holder
            viewHolder.id = cell.findViewById(R.id.id);
            viewHolder.descricao = cell.findViewById(R.id.descricao);
            viewHolder.loja = cell.findViewById(R.id.loja);
            viewHolder.preco = cell.findViewById(R.id.preco);
            viewHolder.edit = cell.findViewById(R.id.edit_produto);
            viewHolder.remove = cell.findViewById(R.id.remove_produto);
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

        if (null == produto)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.descricao.setText(produto.getBebida().getMarca().getNome() + " - " +
                produto.getBebida().getModelo().getNome() + " - " +
                produto.getBebida().getModelo().getVolume() + "ml");
        viewHolder.loja.setText(produto.getLoja().getNome());
        viewHolder.preco.setText("R$" + String.format("%.2f", produto.getPrecoUnidade()));

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CadastroCestaActivity.class);
                intent.putExtra("idProduto", produto.getId());
                getContext().startActivity(intent);
            }
        });

        final FoldingCell finalCell = cell;
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new ProdutoRepository(getContext())).delete(produto.getId());
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
        TextView id;
        TextView descricao;
        TextView loja;
        TextView preco;
        LinearLayout edit;
        Button remove;
    }
}