package br.edu.ifba.mybeerapp.persistence.api;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;

import br.edu.ifba.mybeerapp.model.Produto;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;


@RunWith(AndroidJUnit4.class)
public class ProdutoPersistenceTest
{
    private Context appContext;

    public ProdutoPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
        RepositoryLoader repositoryLoader = RepositoryLoader.getInstance();
        repositoryLoader.setPackageName(RepositoryLoader.API_METHOD);
    }

    @Test
    public void createProduto()
    {
        IProdutoRepository ProdutoRepository = RepositoryLoader.getInstance().getProdutoRepository();

        Produto produto = new Produto();

        Assert.assertNotEquals(-1, ProdutoRepository.create(produto));
    }

    @Test
    public void retrieveProdutoById() {
        IProdutoRepository ProdutoRepository = RepositoryLoader.getInstance().getProdutoRepository();

        Produto produto = (Produto) ProdutoRepository.retrieveById(1);

        Assert.assertNotEquals(null, produto);
    }

    @Test
    public void retrieveAll()
    {
        IProdutoRepository produtoRepository = RepositoryLoader.getInstance().getProdutoRepository();

        produtoRepository.setViewCallback(new ViewCallback() {
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

        produtoRepository.retrieveAll();
    }

    @Test
    public void updateProduto()
    {
        IProdutoRepository ProdutoRepository = RepositoryLoader.getInstance().getProdutoRepository();

        Produto Produto = new Produto();

        //Assert.assertNotEquals(-1, ProdutoRepository.update(Produto));
    }

    @Test
    public void deleteProduto()
    {
        IProdutoRepository ProdutoRepository = RepositoryLoader.getInstance().getProdutoRepository();
        int result = ProdutoRepository.delete(3);

        Assert.assertEquals(1, result);
    }
}
