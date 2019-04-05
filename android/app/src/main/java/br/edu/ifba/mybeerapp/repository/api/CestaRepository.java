package br.edu.ifba.mybeerapp.repository.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.Callback;
import br.edu.ifba.mybeerapp.repository.api.utils.CestaUtilsAPI;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import cz.msebera.android.httpclient.Header;

public class CestaRepository extends Repository implements ICestaRepository {

    public CestaRepository() {
        super("cestas", Cesta.class.getName(), new CestaUtilsAPI());
    }

    @Override
    public int update(IModel oldModel, IModel newModel) {
        Cesta cesta = (Cesta) newModel;

        RequestParams params = new RequestParams();
        params.put("descricao", cesta.getDescricao());

        final Callback callback = super.getOnCreateCallback(this.viewCallback);
        this.client.patch(super.API_URL + "cestas/" + cesta.getId() + "/", params,
                new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if(statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return 0;
    }

    @Override
    public void addProduto(Produto produto) {
        RequestParams params = new RequestParams();
        params.put("cesta", produto.getCestaId());
        params.put("produto", produto.getId());
        params.put("preco", produto.getPrecoUnidade());
        params.put("quantidade", produto.getQtde());

        final Callback callback = super.getOnCreateCallback(this.viewCallback);
        this.client.post(super.API_URL + "produtos_cesta/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if(statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateProduto(Produto produto) {
        RequestParams params = new RequestParams();
        params.put("cesta", produto.getCestaId());
        params.put("produto", produto.getId());
        params.put("preco", produto.getPrecoUnidade());
        params.put("quantidade", produto.getQtde());

        final Callback callback = super.getOnCreateCallback(this.viewCallback);
        this.client.put(super.API_URL + "produtos_cesta/" + produto.getProdutoCestaId() + "/",
                params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if(statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteProduto(Produto produto) {
        super.setEndpoint("produtos_cesta");
        super.delete(produto.getProdutoCestaId());
    }
}