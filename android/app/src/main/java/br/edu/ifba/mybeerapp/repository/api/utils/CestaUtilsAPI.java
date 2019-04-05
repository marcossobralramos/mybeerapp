package br.edu.ifba.mybeerapp.repository.api.utils;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.annotations.DBFieldName;
import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.IUtilsAPI;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class CestaUtilsAPI extends IUtilsAPI {
    @Override
    public RequestParams toRequestParams(IModel model, RequestParams params) {
        if (params == null)
            params = new RequestParams();

        Field fields[] = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(DBField.class) == false)
                continue;

            String fieldName = field.getName();
            String modelClassName = fieldName.substring(0, 1).toUpperCase() +
                    fieldName.substring(1, fieldName.length());
            String getMethodName = UtilsAPI.loadMethodName(fieldName, "get");

            // verificando se o field tem um nome já definido na API
            String apiFieldName = (field.isAnnotationPresent(DBFieldName.class)) ?
                    field.getAnnotation(DBFieldName.class).name() :
                    fieldName;

            Method method = null;
            try {
                method = model.getClass().getMethod(getMethodName);
            } catch (NoSuchMethodException e) {
                continue;
            }
            String value = "";
            try {
                // se a field for do tipo model, pega-se o seu id
                if (Class.forName(super.PACKAGE_NAME_MODEL + "." + modelClassName).newInstance() != null) {
                    IModel subModel = (IModel) method.invoke(model);
                    value = String.valueOf(subModel.getId());
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                try {
                    value = String.valueOf(method.invoke(model));
                } catch (IllegalAccessException | InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            params.put(apiFieldName, value);
        }

        return params;
    }

    @Override
    public IModel toModel(JSONObject response, String modelClassName) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(response);
        return (IModel) this.createListModel(jsonArray, null).toArray()[0];
    }

    @Override
    public Collection<IModel> createListModel(JSONArray response, String modelClassName) {
        List<IModel> models = new ArrayList<>();

        for (int x = 0; x < response.length(); x++) {
            Cesta cesta = new Cesta();

            try {
                JSONObject jsonModel = response.getJSONObject(x);
                cesta.setId(jsonModel.getInt("id"));
                cesta.setDescricao(jsonModel.getString("descricao"));

                JSONArray produtos = jsonModel.getJSONArray("produtos");
                for (int y = 0; y < produtos.length(); y++) {
                    Produto produto = new Produto();
                    JSONObject jsonProduto = produtos.getJSONObject(y);
                    produto.setId(jsonProduto.getInt("id"));
                    produto.setCestaId(cesta.getId());
                    produto.setUltimaAtualizacao(jsonProduto.getString("ultima_atualizacao"));
                    produto.setProdutoCestaId(jsonProduto.getInt("id_relacionamento_produto_cesta"));

                    // setando a loja do produto
                    JSONObject jsonLoja = jsonProduto.getJSONObject("loja");
                    produto.setLoja(
                            new Loja(jsonLoja.getInt("id"), jsonLoja.getString("nome"))
                    );

                    // setando a bebida relacionada com o produto
                    JSONObject jsonBebida = jsonProduto.getJSONObject("bebida");
                    Bebida bebida = new Bebida();
                    bebida.setId(jsonBebida.getInt("id"));

                    // setando a marca da bebida
                    JSONObject jsonMarca = jsonBebida.getJSONObject("marca");
                    bebida.setMarca(new Marca(
                            jsonMarca.getInt("id"), jsonMarca.getString("nome")
                    ));

                    // setando o modelo da bebida
                    JSONObject jsonModelo = jsonBebida.getJSONObject("modelo");
                    bebida.setModelo(new Modelo(
                            jsonModelo.getInt("id"),
                            jsonModelo.getString("descricao"),
                            jsonModelo.getInt("volume")
                    ));

                    produto.setBebida(bebida);
                    produto.setPrecoUnidade(jsonProduto.getDouble("preco_unidade"));

                    // add produto na cesta
                    cesta.addProduto(produto, jsonProduto.getInt("quantidade"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            models.add((IModel) cesta);
        }

        return models;
    }
}