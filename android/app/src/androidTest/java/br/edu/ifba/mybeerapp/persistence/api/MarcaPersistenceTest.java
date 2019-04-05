package br.edu.ifba.mybeerapp.persistence.api;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IMarcaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.MarcaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.Repository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;


@RunWith(AndroidJUnit4.class)
public class MarcaPersistenceTest
{
    private Context appContext;

    public MarcaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
        RepositoryLoader repositoryLoader = RepositoryLoader.getInstance();
        repositoryLoader.setPackageName(RepositoryLoader.API_METHOD);
    }

    @Test
    public void createMarca()
    {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository();

        Marca marca = new Marca("Skol");

        Assert.assertNotEquals(-1, marcaRepository.create(marca));
    }

    @Test
    public void retrieveMarcaById() {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository();

        Marca marca = (Marca) marcaRepository.retrieveById(1);

        Assert.assertNotEquals(null, marca);
    }

    @Test
    public void retrieveAll()
    {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository();

        marcaRepository.setViewCallback(new ViewCallback() {
            @Override
            public void success(ArrayList<IModel> models) {
                Assert.assertNotEquals(0, models.size());
            }

            @Override
            public void success(IModel model) {

            }

            @Override
            public void fail(String message) {

            }
        });

        marcaRepository.retrieveAll();
    }

    @Test
    public void updateMarca()
    {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository();

        Marca marca = new Marca("SkolUpdate");

        //Assert.assertNotEquals(-1, marcaRepository.update(marca));
    }

    @Test
    public void deleteMarca()
    {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository();
        int result = marcaRepository.delete(3);

        Assert.assertEquals(1, result);
    }
}
