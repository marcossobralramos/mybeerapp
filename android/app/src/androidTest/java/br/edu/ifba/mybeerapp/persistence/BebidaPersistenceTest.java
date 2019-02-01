package br.edu.ifba.mybeerapp.persistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;


@RunWith(AndroidJUnit4.class)
public class BebidaPersistenceTest
{
    private Context appContext;

    public BebidaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createBebida() throws IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException {
        BebidaRepository bebidaRepository = new BebidaRepository(this.appContext);

        Marca marca = new Marca();
        marca.setId(3);

        Modelo modelo = new Modelo();
        modelo.setId(3);

        Bebida bebida = new Bebida(marca, modelo);

        Assert.assertNotEquals(-1, bebidaRepository.create(bebida));
    }

    @Test
    public void retrieveBebidaById() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        BebidaRepository bebidaRepository = new BebidaRepository(this.appContext);

        Bebida bebida = (Bebida) bebidaRepository.retrieveById(1);

        Assert.assertNotEquals(null, bebida);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchMethodException,
            ClassNotFoundException
    {
        BebidaRepository bebidaRepository = new BebidaRepository(this.appContext);

        List<IModel> bebidas = bebidaRepository.retrieveAll();

        Assert.assertNotEquals(0, bebidas.size());
    }

    @Test
    public void updateBebida() throws IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        BebidaRepository bebidaRepository = new BebidaRepository(this.appContext);

        Marca marca = new Marca();
        marca.setId(2);

        Modelo modelo = new Modelo();
        modelo.setId(1);

        Bebida bebida = new Bebida(marca, modelo);

        //Assert.assertNotEquals(-1, bebidaRepository.update(bebida));
    }

    @Test
    public void deleteBebida()
    {
        BebidaRepository bebidaRepository = new BebidaRepository(this.appContext);
        int result = bebidaRepository.delete(1);

        Assert.assertEquals(1, result);
    }
}
