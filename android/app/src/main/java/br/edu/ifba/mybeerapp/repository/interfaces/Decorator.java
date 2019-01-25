package br.edu.ifba.mybeerapp.repository.interfaces;

import java.lang.reflect.InvocationTargetException;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;

public interface Decorator
{
    Object retrieveModel(String typeName, String id) throws
            IllegalAccessException, InvocationTargetException,
            InstantiationException, ColunmTypeNotKnownException,
            NoSuchMethodException, ClassNotFoundException;
}
