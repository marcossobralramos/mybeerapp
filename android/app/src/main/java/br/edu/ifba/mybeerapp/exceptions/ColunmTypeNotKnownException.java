package br.edu.ifba.mybeerapp.exceptions;

public class ColunmTypeNotKnownException extends Exception
{
    public ColunmTypeNotKnownException(String type)
    {
        super("O tipo '" + type + "' é desconhecido.");
    }
}
