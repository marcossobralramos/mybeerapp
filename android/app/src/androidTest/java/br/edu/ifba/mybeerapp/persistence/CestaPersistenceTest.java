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

import br.edu.ifba.mybeerapp.model.Cesta;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.CestaRepository;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;


@RunWith(AndroidJUnit4.class)
public class CestaPersistenceTest
{
    private Context appContext;

    public CestaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createCesta() throws IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException {
        CestaRepository cestaRepository = new CestaRepository(this.appContext);
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);


        Cesta cesta = new Cesta();
        cesta.setDescricao("Reggae - Casa de Mila");

        Produto prod1 = (Produto) produtoRepository.retrieveById(1);
        cesta.addProduto(prod1, 10);

        Produto prod2 = (Produto) produtoRepository.retrieveById(2);
        cesta.addProduto(prod2, 8);

        Produto prod3 = (Produto) produtoRepository.retrieveById(3);
        cesta.addProduto(prod3, 10);

        Cesta cesta2 = new Cesta();
        cesta.setDescricao("Reggae - Casa de Mila");

        Produto prod11 = (Produto) produtoRepository.retrieveById(1);
        cesta.addProduto(prod1, 10);

        Produto prod12 = (Produto) produtoRepository.retrieveById(2);
        cesta.addProduto(prod2, 8);

        Produto prod13 = (Produto) produtoRepository.retrieveById(3);
        cesta.addProduto(prod3, 10);

        Assert.assertNotEquals(-1, cestaRepository.create(cesta));
    }

    @Test
    public void retrieveCestaById() throws IllegalAccessException, InvocationTargetException,
            InstantiationException,  NoSuchMethodException, ClassNotFoundException
    {
        CestaRepository cestaRepository = new CestaRepository(this.appContext);

        Cesta cesta = (Cesta) cestaRepository.retrieveById(5);

        Assert.assertNotEquals(null, cesta);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException,  NoSuchMethodException,
            ClassNotFoundException
    {
        CestaRepository cestaRepository = new CestaRepository(this.appContext);

        List<IModel> cestas = cestaRepository.retrieveAll();

        Assert.assertNotEquals(0, cestas.size());
    }

    @Test
    public void updateCesta() throws  IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        CestaRepository cestaRepository = new CestaRepository(this.appContext);

        Cesta cesta = new Cesta();

        // Assert.assertNotEquals(-1, cestaRepository.update(cesta));
    }

    @Test
    public void deleteCesta()
    {
        CestaRepository cestaRepository = new CestaRepository(this.appContext);
        int result = cestaRepository.delete(6);

        Assert.assertEquals(1, result);
    }
}
