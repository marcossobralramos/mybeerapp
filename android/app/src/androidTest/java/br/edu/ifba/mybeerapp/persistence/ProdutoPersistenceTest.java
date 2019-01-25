package br.edu.ifba.mybeerapp.persistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;
import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.Loja;
import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.Marca;
import br.edu.ifba.mybeerapp.model.Modelo;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.BebidaRepository;
import br.edu.ifba.mybeerapp.repository.LojaRepository;
import br.edu.ifba.mybeerapp.repository.ProdutoRepository;

@RunWith(AndroidJUnit4.class)
public class ProdutoPersistenceTest
{
    private Context appContext;

    public ProdutoPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createProduto() throws ColunmTypeNotKnownException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);

        Loja loja = (Loja) (new LojaRepository(this.appContext)).retrieveById(3);
        Bebida bebida = (Bebida) (new BebidaRepository(this.appContext)).retrieveById(3);

        Produto produto = new Produto();
        produto.setLoja(loja);
        produto.setBebida(bebida);
        produto.setPrecoUnidade(4.40);
        produto.setUltimaAtualizacao("25/01/2019");

        Assert.assertNotEquals(-1, produtoRepository.create(produto));
    }

    @Test
    public void retrieveProdutoById() throws IllegalAccessException, InvocationTargetException, InstantiationException, ColunmTypeNotKnownException, NoSuchMethodException, ClassNotFoundException {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);

        Produto produto = (Produto) produtoRepository.retrieveById(2);

        Assert.assertNotEquals(null, produto);
    }

    @Test
    public void retrieveAll() throws IllegalAccessException, InvocationTargetException,
            InstantiationException, ColunmTypeNotKnownException, NoSuchMethodException,
            ClassNotFoundException
    {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);

        List<IModel> produtos = produtoRepository.retrieveAll();

        Assert.assertNotEquals(0, produtos.size());
    }

    @Test
    public void updateProduto() throws ColunmTypeNotKnownException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException,
            InvocationTargetException, IOException
    {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);

        Produto produto = new Produto();

        //Assert.assertNotEquals(-1, produtoRepository.update(produto));
    }

    @Test
    public void deleteProduto()
    {
        ProdutoRepository produtoRepository = new ProdutoRepository(this.appContext);
        int result = produtoRepository.delete(0);

        Assert.assertEquals(1, result);
    }
}
