package br.edu.ifba.mybeerapp.persistence.sqlite;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IMarcaRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.MarcaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;


@RunWith(AndroidJUnit4.class)
public class MarcaPersistenceTest
{
    private Context appContext;

    public MarcaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
        RepositoryLoader repositoryLoader = RepositoryLoader.getInstance();
        repositoryLoader.setPackageName(RepositoryLoader.SQLITE_METHOD);
    }

    @Test
    public void createMarca()
    {
        IMarcaRepository marcaRepository = RepositoryLoader.getInstance().getMarcaRepository(this.appContext);

        Marca marca = new Marca("Skol");

        Assert.assertNotEquals(-1, marcaRepository.create(marca));
    }

    @Test
    public void retrieveMarcaById() throws IllegalAccessException, InvocationTargetException, InstantiationException,  NoSuchMethodException, ClassNotFoundException {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        Marca marca = (Marca) marcaRepository.retrieveById(1);

        Assert.assertNotEquals(null, marca);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException,  NoSuchMethodException,
            ClassNotFoundException
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        List<IModel> marcas = marcaRepository.retrieveAll();

        Assert.assertNotEquals(0, marcas.size());
    }

    @Test
    public void updateMarca() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        Marca marca = new Marca("SkolUpdate");

        //Assert.assertNotEquals(-1, marcaRepository.update(marca));
    }

    @Test
    public void deleteMarca()
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);
        int result = marcaRepository.delete(3);

        Assert.assertEquals(1, result);
    }
}
