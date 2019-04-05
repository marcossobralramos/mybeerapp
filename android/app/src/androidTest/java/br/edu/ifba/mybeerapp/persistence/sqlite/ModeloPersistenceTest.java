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

import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.sqlite.ModeloRepository;


@RunWith(AndroidJUnit4.class)
public class ModeloPersistenceTest
{
    private Context appContext;

    public ModeloPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createModelo() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        ModeloRepository modeloRepository = new ModeloRepository(this.appContext);

        Modelo modelo = new Modelo("Bujudinha", 350);

        Assert.assertNotEquals(-1, modeloRepository.create(modelo));
    }

    @Test
    public void retrieveModeloById() throws IllegalAccessException, InvocationTargetException, InstantiationException,  NoSuchMethodException, ClassNotFoundException {
        ModeloRepository modeloRepository = new ModeloRepository(this.appContext);

        Modelo modelo = (Modelo) modeloRepository.retrieveById(1);

        Assert.assertNotEquals(null, modelo);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException,  NoSuchMethodException,
            ClassNotFoundException
    {
        ModeloRepository modeloRepository = new ModeloRepository(this.appContext);

        List<IModel> modelos = modeloRepository.retrieveAll();

        Assert.assertNotEquals(0, modelos.size());
    }

    @Test
    public void updateModelo() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        ModeloRepository modeloRepository = new ModeloRepository(this.appContext);

        Modelo modelo = new Modelo("Bujuda", 350);

        //Assert.assertNotEquals(-1, modeloRepository.update(modelo));
    }

    @Test
    public void deleteModelo()
    {
        ModeloRepository modeloRepository = new ModeloRepository(this.appContext);
        int result = modeloRepository.delete(1);

        Assert.assertEquals(1, result);
    }
}
