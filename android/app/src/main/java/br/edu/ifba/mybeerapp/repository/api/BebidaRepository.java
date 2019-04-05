package br.edu.ifba.mybeerapp.repository.api;

import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;

public class BebidaRepository extends Repository implements IBebidaRepository
{
    public BebidaRepository(){
        super("bebidas", Bebida.class.getName());
    }
}