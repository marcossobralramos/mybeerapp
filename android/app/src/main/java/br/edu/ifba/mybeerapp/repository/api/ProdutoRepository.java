package br.edu.ifba.mybeerapp.repository.api;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.Callback;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;

public class ProdutoRepository extends Repository implements IProdutoRepository
{
    public ProdutoRepository(){
        super("produtos", Produto.class.getName());
    }

    @Override
    public List<IModel> retrieveByLoja(final int idLoja) {
        super.setDefaultOnGetAllCallback(new Callback<JSONArray>() {
            @Override
            public void success(JSONArray response) {
                ArrayList<IModel> models = (ArrayList<IModel>)
                        utilsAPI.createListModel(response, Produto.class.getName());
                ArrayList<IModel> produtosByLoja = new ArrayList<>();
                for(IModel produto : models) {
                    Produto prod = (Produto) produto;
                    if(prod.getLoja().getId() == idLoja)
                        produtosByLoja.add(produto);
                }

                if(getViewCallback() != null)
                    getViewCallback().success(produtosByLoja);

                setDefaultOnGetAllCallback(null); // resetando on get all callback
            }

            @Override
            public void fail(String message) {
                if(getViewCallback() != null)
                    getViewCallback().fail(message);
            }
        });
        super.retrieveAll();
        return null;
    }
}