package br.edu.ifba.mybeerapp.repository.interfaces;

import java.util.List;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;

public interface IRepository
{
    long create(IModel model);
    int update(IModel oldModel, IModel newModel);
    int delete(int id);
    List<IModel> retrieveAll();
    IModel retrieveById(int id);
    Object retrieveModel(String typeName, String id);
    void setViewCallback(ViewCallback viewCallback);
    ViewCallback getViewCallback();
}
