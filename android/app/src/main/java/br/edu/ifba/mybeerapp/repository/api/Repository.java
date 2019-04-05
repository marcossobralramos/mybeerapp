package br.edu.ifba.mybeerapp.repository.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.api.interfaces.Callback;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.IUtilsAPI;
import br.edu.ifba.mybeerapp.repository.interfaces.IRepository;
import br.edu.ifba.mybeerapp.repository.api.utils.UtilsAPI;
import cz.msebera.android.httpclient.Header;

public class Repository implements IRepository {
    protected String modelClassName;
    protected ViewCallback viewCallback;
    protected final String API_URL = "https://sobral.pythonanywhere.com/";
    protected AsyncHttpClient client;

    protected String endpoint;
    protected IUtilsAPI utilsAPI;

    public Callback getDefaultOnGetAllCallback() {
        return defaultOnGetAllCallback;
    }

    public void setDefaultOnGetAllCallback(Callback defaultOnGetAllCallback) {
        this.defaultOnGetAllCallback = defaultOnGetAllCallback;
    }

    protected Callback defaultOnGetAllCallback;

    public Repository(String rota, String modelClassName, IUtilsAPI utilsAPI) {
        this.modelClassName = modelClassName;
        this.client = new AsyncHttpClient();
        this.endpoint = API_URL + rota + "/";
        this.utilsAPI = utilsAPI;
    }

    public Repository(String rota, String modelClassName) {
        this.modelClassName = modelClassName;
        this.client = new AsyncHttpClient();
        this.endpoint = API_URL + rota + "/";
        this.utilsAPI = new UtilsAPI();
    }

    public long create(final IModel model) {
        final RequestParams params = this.utilsAPI.toRequestParams(model, null);
        final Callback callback = this.getOnCreateCallback(this.viewCallback);
        this.client.post(this.endpoint, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if (statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String message, Throwable th) {
                if(statusCode == 500)
                    getViewCallback().success(model);
            }
        });
        return 0;
    }

    @Override
    public int update(IModel oldModel, IModel newModel) {
        newModel.setId(oldModel.getId());
        final RequestParams params = this.utilsAPI.toRequestParams(newModel, null);
        final Callback callback = this.getOnCreateCallback(this.viewCallback);
        this.client.put(this.endpoint + oldModel.getId() + "/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if (statusCode == 403) {
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
    public int delete(int id) {
        final Callback callback = this.getOnCreateCallback(this.viewCallback);
        this.client.delete(this.endpoint + id + "/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if (statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.fail(responseString);
            }
        });
        return 0;
    }

    @Override
    public List<IModel> retrieveAll() {
        final Callback callback = this.getOnGetAllCallback(this.viewCallback);
        this.client.get(this.endpoint, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if (statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    public IModel retrieveById(int id) {
        final Callback callback = this.getOnGetSingleCallback(this.viewCallback);
        this.client.get(this.endpoint + id + "/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.success(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable th, JSONObject response) {
                try {
                    if (statusCode == 403) {
                        callback.fail(response.getString("detail"));
                        return;
                    }
                    callback.fail(th.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    public Object retrieveModel(String typeName, String id) {
        return null;
    }

    @Override
    public ViewCallback getViewCallback() {
        return viewCallback;
    }

    public void setViewCallback(ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = this.API_URL + endpoint + "/";
    }

    protected Callback getOnGetAllCallback(final ViewCallback viewCallback) {
        if (this.defaultOnGetAllCallback != null)
            return this.defaultOnGetAllCallback;

        final String modelClass = this.modelClassName;
        final IUtilsAPI utilsAPI = this.utilsAPI;
        return new Callback<JSONArray>() {
            @Override
            public void success(JSONArray response) {
                ArrayList<IModel> models = (ArrayList<IModel>)
                        utilsAPI.createListModel(response, modelClass);
                if (viewCallback != null)
                    viewCallback.success(models);
            }

            @Override
            public void fail(String message) {
                if (viewCallback != null)
                    viewCallback.fail(message);
            }
        };
    }

    protected Callback getOnGetSingleCallback(final ViewCallback viewCallback) {
        final String modelClass = this.modelClassName;
        final IUtilsAPI utilsAPI = this.utilsAPI;
        return new Callback<JSONObject>() {
            @Override
            public void success(JSONObject response) {
                IModel model = (IModel) utilsAPI.toModel(response, modelClass);
                if (viewCallback != null)
                    viewCallback.success(model);
            }

            @Override
            public void fail(String message) {
                if (viewCallback != null)
                    viewCallback.fail(message);
            }
        };
    }

    protected Callback getOnCreateCallback(final ViewCallback viewCallback) {
        final String modelClass = this.modelClassName;
        final IUtilsAPI utilsAPI = this.utilsAPI;
        return new Callback<JSONObject>() {
            @Override
            public void success(JSONObject response) {
                IModel model = (IModel) utilsAPI.toModel(response, modelClass);
                if (viewCallback != null)
                    viewCallback.success(model);
            }

            @Override
            public void fail(String message) {
                if (viewCallback != null)
                    viewCallback.fail(message);
            }
        };
    }
}