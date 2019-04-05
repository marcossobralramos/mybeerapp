package br.edu.ifba.mybeerapp.repository.api;

import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.repository.interfaces.IMarcaRepository;

public class MarcaRepository extends Repository implements IMarcaRepository
{
    public MarcaRepository(){
        super("marcas", Marca.class.getName());
    }
}