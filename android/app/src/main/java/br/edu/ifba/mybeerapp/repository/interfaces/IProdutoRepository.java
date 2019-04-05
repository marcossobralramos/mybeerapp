package br.edu.ifba.mybeerapp.repository.interfaces;

import java.util.List;

import br.edu.ifba.mybeerapp.model.interfaces.IModel;

public interface IProdutoRepository extends IRepository {
    List<IModel> retrieveByLoja(int idLoja);
}
