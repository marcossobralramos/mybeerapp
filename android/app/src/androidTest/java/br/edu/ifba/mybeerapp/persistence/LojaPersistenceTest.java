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

import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.LojaRepository;


@RunWith(AndroidJUnit4.class)
public class LojaPersistenceTest
{
    private Context appContext;

    public LojaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createLoja() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        LojaRepository lojaRepository = new LojaRepository(this.appContext);

        Loja loja = new Loja("Extra - Paralela");

        Assert.assertNotEquals(-1, lojaRepository.create(loja));
    }

    @Test
    public void retrieveLojaById() throws IllegalAccessException, InvocationTargetException, InstantiationException,  NoSuchMethodException, ClassNotFoundException {
        LojaRepository lojaRepository = new LojaRepository(this.appContext);

        Loja loja = (Loja) lojaRepository.retrieveById(2);

        Assert.assertNotEquals(null, loja);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException,  NoSuchMethodException,
            ClassNotFoundException
    {
        LojaRepository lojaRepository = new LojaRepository(this.appContext);

        List<IModel> lojas = lojaRepository.retrieveAll();

        Assert.assertNotEquals(0, lojas.size());
    }

    @Test
    public void updateLoja() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        LojaRepository lojaRepository = new LojaRepository(this.appContext);

        Loja loja = new Loja("Bompreço - Brotas");

        //Assert.assertNotEquals(-1, lojaRepository.update(loja));
    }

    @Test
    public void deleteLoja()
    {
        LojaRepository lojaRepository = new LojaRepository(this.appContext);
        int result = lojaRepository.delete(0);

        Assert.assertEquals(1, result);
    }
}
