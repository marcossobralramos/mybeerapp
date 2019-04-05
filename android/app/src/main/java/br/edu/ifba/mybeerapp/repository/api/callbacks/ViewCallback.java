package br.edu.ifba.mybeerapp.repository.api.callbacks;

import java.util.ArrayList;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.interfaces.Callback;

public interface ViewCallback extends Callback<ArrayList<IModel>> {
    @Override
    public void success(ArrayList<IModel> models);
    public void success(IModel model);
    public void fail(String message);
}