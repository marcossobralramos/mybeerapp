package br.edu.ifba.mybeerapp.repository.api.interfaces;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public abstract class IUtilsAPI {
    public final String PACKAGE_NAME_MODEL = "br.edu.ifba.mybeerapp.model";

    public IUtilsAPI() {}

    protected static String loadMethodName(String fieldName, String prefix) {
        String[] palavras = fieldName.split("_");
        String methodName = prefix;
        for (String palavra : palavras) {
            methodName += palavra.substring(0, 1).toUpperCase() +
                    palavra.substring(1, palavra.length());
        }
        return methodName;
    }

    protected static String loadModelFieldName(String fieldName) {
        String[] palavras = fieldName.split("_");
        String modelFieldName = palavras[0];

        for (int x = 1; x < palavras.length; x++) {
            modelFieldName += palavras[x].substring(0, 1).toUpperCase() +
                    palavras[x].substring(1, palavras[x].length());
        }

        return modelFieldName;
    }

    public abstract RequestParams toRequestParams(IModel model, RequestParams params);
    public abstract IModel toModel(JSONObject response, String modelClassName);
    public abstract Collection<IModel> createListModel(JSONArray response, String modelClassName);

}
