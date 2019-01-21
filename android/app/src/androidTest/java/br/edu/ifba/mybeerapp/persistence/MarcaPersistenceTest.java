package br.edu.ifba.mybeerapp.persistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.repository.MarcaRepository;


@RunWith(AndroidJUnit4.class)
public class MarcaPersistenceTest
{
    private Context appContext;

    public MarcaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createMarca() throws ColunmTypeNotKnownException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        Marca marca = new Marca(1, "Skol");
        marcaRepository.create(marca);

        Marca marcaUpdated = (Marca) marcaRepository.retrieveById(1);

        Assert.assertEquals(marcaUpdated, marca);
    }

    @Test
    public void retrieveMarcaById() throws IllegalAccessException, InvocationTargetException, InstantiationException, ColunmTypeNotKnownException, NoSuchMethodException, ClassNotFoundException {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        Marca marca = (Marca) marcaRepository.retrieveById(1);

        Assert.assertNotEquals(null, marca);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException, ColunmTypeNotKnownException, NoSuchMethodException,
            ClassNotFoundException
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        List<Object> marcas = marcaRepository.retrieveAll();

        Assert.assertNotEquals(0, marcas.size());
    }

    @Test
    public void updateMarca() throws ColunmTypeNotKnownException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);

        Marca marca = new Marca(1, "SkolUpdate");
        marcaRepository.update(marca);

        Marca marcaCreated = (Marca) marcaRepository.retrieveById(1);

        Assert.assertEquals(marcaCreated, marca);
    }

    @Test
    public void deleteMarca()
    {
        MarcaRepository marcaRepository = new MarcaRepository(this.appContext);
        int result = marcaRepository.delete(1);

        Assert.assertEquals(1, result);
    }
}
