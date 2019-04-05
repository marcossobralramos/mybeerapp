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

import br.edu.ifba.mybeerapp.model.annotations.DBField;
import br.edu.ifba.mybeerapp.model.annotations.DBFieldName;
import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.IUtilsAPI;
import br.edu.ifba.mybeerapp.utils.UtilsDB;

public class UtilsAPI extends IUtilsAPI {
    @Override
    public RequestParams toRequestParams(IModel model, RequestParams params) {
        if(params == null)
            params = new RequestParams();

        Field fields[] = model.getClass().getDeclaredFields();

        for(Field field : fields) {
            if(field.isAnnotationPresent(DBField.class) == false)
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
                if(Class.forName(super.PACKAGE_NAME_MODEL + "." + modelClassName).newInstance() != null) {
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
            params.add(apiFieldName, value);
        }

        return params;
    }

    @Override
    public IModel toModel(JSONObject response, String modelClassName) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(response);
        return (IModel) this.createListModel(jsonArray, modelClassName).toArray()[0];
    }

    @Override
    public Collection<IModel> createListModel(JSONArray response, String modelClassName) {
        List<IModel> models = new ArrayList<>();

        try {
            for (int x = 0; x < response.length(); x++) {
                Object model = Class.forName(modelClassName).newInstance();
                JSONObject jsonModel = response.getJSONObject(x);

                Iterator<String> keys = jsonModel.keys();

                while(keys.hasNext())
                {
                    String fieldName = keys.next();
                    String methodName = UtilsAPI.loadMethodName(fieldName, "set");
                    String modelFieldName = UtilsAPI.loadModelFieldName(fieldName);
                    Class fieldClassType = model.getClass().getDeclaredField(modelFieldName).getType();
                    String fieldTypeName = fieldClassType.getName();
                    String modelName = fieldName.substring(0, 1).toUpperCase() +
                            fieldName.substring(1, fieldName.length());

                    Method method = model.getClass().getMethod(methodName, fieldClassType);
                    if (method.isAnnotationPresent(RepositoryNotAccess.class))
                        continue;

                    try {
                        IModel subModel = (IModel) Class.forName(UtilsDB.PACKAGE_NAME_MODEL +
                                "." + modelName).newInstance();

                        if (subModel != null) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(jsonModel.getJSONObject(fieldName));
                            subModel = (IModel) this.createListModel(jsonArray
                                    , subModel.getClass().getName()).toArray()[0];
                            method.invoke(model, subModel);
                        }
                    } catch (ClassNotFoundException ex) {
                        try {
                            Object value;

                            if (String.class.getName().equals(fieldTypeName))
                                value = (String) jsonModel.getString(fieldName);
                            else if (Integer.class.getName().equals(fieldTypeName) || int.class.getName().equals(fieldTypeName))
                                value = (int) jsonModel.getInt(fieldName);
                            else if (Double.class.getName().equals(fieldTypeName) || double.class.getName().equals(fieldTypeName))
                                value = (double) jsonModel.getDouble(fieldName);
                            else if (Float.class.getName().equals(fieldTypeName) || float.class.getName().equals(fieldTypeName))
                                value = (float) jsonModel.getDouble(fieldName);
                            else if (Long.class.getName().equals(fieldTypeName) || long.class.getName().equals(fieldTypeName))
                                value = (long) jsonModel.getLong(fieldName);
                            else if (Short.class.getName().equals(fieldTypeName) || short.class.getName().equals(fieldTypeName))
                                value = (short) jsonModel.getInt(fieldName);
                            else {
                                continue;
                            }

                            method.invoke(model, value);

                        } catch (JSONException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                models.add((IModel) model);
            }

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | JSONException |
                NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return models;
    }
}