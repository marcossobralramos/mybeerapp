package br.edu.ifba.mybeerapp.repository.interfaces;

import br.edu.ifba.mybeerapp.model.Produto;

public interface ICestaRepository extends IRepository {
    void deleteProduto(Produto produto);
    void addProduto(Produto produto);
    void updateProduto(Produto produto);
}
