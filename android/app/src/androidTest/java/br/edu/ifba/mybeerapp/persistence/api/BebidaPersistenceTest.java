package br.edu.ifba.mybeerapp.persistence.api;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;

import br.edu.ifba.mybeerapp.model.Bebida;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.api.callbacks.ViewCallback;
import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;
import br.edu.ifba.mybeerapp.utils.RepositoryLoader;


@RunWith(AndroidJUnit4.class)
public class BebidaPersistenceTest
{
    private Context appContext;

    public BebidaPersistenceTest()
    {
        this.appContext = InstrumentationRegistry.getTargetContext();
        RepositoryLoader repositoryLoader = RepositoryLoader.getInstance();
        repositoryLoader.setPackageName(RepositoryLoader.API_METHOD);
    }

    @Test
    public void createBebida()
    {
        IBebidaRepository BebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();

        Bebida bebida = new Bebida();

        Assert.assertNotEquals(-1, BebidaRepository.create(bebida));
    }

    @Test
    public void retrieveBebidaById() {
        IBebidaRepository BebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();

        Bebida bebida = (Bebida) BebidaRepository.retrieveById(1);

        Assert.assertNotEquals(null, bebida);
    }

    @Test
    public void retrieveAll()
    {
        IBebidaRepository bebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();

        bebidaRepository.setViewCallback(new ViewCallback() {
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

        bebidaRepository.retrieveAll();
    }

    @Test
    public void updateBebida()
    {
        IBebidaRepository BebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();

        Bebida Bebida = new Bebida();

        //Assert.assertNotEquals(-1, BebidaRepository.update(Bebida));
    }

    @Test
    public void deleteBebida()
    {
        IBebidaRepository BebidaRepository = RepositoryLoader.getInstance().getBebidaRepository();
        int result = BebidaRepository.delete(3);

        Assert.assertEquals(1, result);
    }
}
