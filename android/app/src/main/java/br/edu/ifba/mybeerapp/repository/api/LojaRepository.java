package br.edu.ifba.mybeerapp.repository.api;

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;

public class LojaRepository extends Repository implements ILojaRepository
{
    public LojaRepository(){
        super("lojas", Loja.class.getName());
    }
}