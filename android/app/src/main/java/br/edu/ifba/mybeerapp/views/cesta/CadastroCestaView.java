package br.edu.ifba.mybeerapp.views.cesta;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import br.edu.ifba.mybeerapp.R;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import br.edu.ifba.mybeerapp.views.produto.ProdutosListActivity;
import foldingcell.FoldingCell;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class CadastroCestaView extends ArrayAdapter<Produto> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public CadastroCestaView(Context context, ArrayList<Produto> objects) {
        super(context, 0, objects);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
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
            cell = (FoldingCell) vi.inflate(R.layout.content_cadastro_cesta, parent, false);
            // binding view parts to view holder

            viewHolder.descricao = cell.findViewById(R.id.descricao);
            viewHolder.loja = cell.findViewById(R.id.loja);
            viewHolder.preco = cell.findViewById(R.id.preco);
            viewHolder.precoLitro = cell.findViewById(R.id.preco_litro);
            viewHolder.qtde = cell.findViewById(R.id.qtde);
            viewHolder.valorTotal = cell.findViewById(R.id.preco_total);
            viewHolder.edit = cell.findViewById(R.id.edit_produto);
            viewHolder.remove = cell.findViewById(R.id.remove_produto);
            viewHolder.totalML = cell.findViewById(R.id.total_ml);
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
        if(produto.getLoja().getNome().length() > 14)
            viewHolder.loja.setText(produto.getLoja().getNome().substring(0,14) + "...");
        else
            viewHolder.loja.setText(produto.getLoja().getNome());
        viewHolder.preco.setText("R$" + String.format("%.2f", produto.getPrecoUnidade()));
        viewHolder.precoLitro.setText("R$" + String.format("%.2f", produto.getPrecoLitro()));
        viewHolder.qtde.setText(String.valueOf(produto.getQtde()));
        viewHolder.valorTotal.setText("R$" + String.format("%.2f", produto.getValorTotal()));
        viewHolder.totalML.setText(String.format("%.0f", produto.getTotalML()) + "ml");
       viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CadastroProdutoCestaActivity.class);
                intent.putExtra("idProduto", produto.getId());
                intent.putExtra("idCesta", produto.getCestaId());
                ((CadastroCestaActivity) getContext()).startActivityForResult(intent, 1);
            }
        });
       viewHolder.remove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int success = (new CestaRepository(getContext())).deleteProduto(produto.getCestaId(), produto.getId());
               if(success != -1) {
                   Toast.makeText(getContext(),
                           "Produto deletado da cesta com sucesso!",
                           Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(getContext(),
                           "Erro ao deletar o produto da cesta!",
                           Toast.LENGTH_SHORT).show();
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
        TextView descricao;
        TextView loja;
        TextView preco;
        TextView precoLitro;
        TextView qtde;
        TextView valorTotal;
        TextView totalML;
        LinearLayout edit;
        Button remove;
    }
}