package br.edu.ifba.mybeerapp.repository.api;

import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.repository.interfaces.IModeloRepository;

public class ModeloRepository extends Repository implements IModeloRepository
{
    public ModeloRepository(){
        super("modelos", Modelo.class.getName());
    }
}