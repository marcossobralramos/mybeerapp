package br.edu.ifba.mybeerapp.repository.api.interfaces;

public interface Callback<T> {
    void success(T t);
    void fail(String message);
}