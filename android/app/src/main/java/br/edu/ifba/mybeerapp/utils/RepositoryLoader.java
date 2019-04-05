package br.edu.ifba.mybeerapp.utils;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import br.edu.ifba.mybeerapp.repository.interfaces.IBebidaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.ICestaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.ILojaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IMarcaRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IModeloRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IProdutoRepository;
import br.edu.ifba.mybeerapp.repository.interfaces.IRepository;
import br.edu.ifba.mybeerapp.repository.sqlite.Repository;

public class RepositoryLoader
{
    private static RepositoryLoader instance = null;
    public static String API_METHOD = "api";
    public static String SQLITE_METHOD = "sqlite";

    private String packageName;

    private RepositoryLoader(){ }

    public static RepositoryLoader getInstance()
    {
        if(instance == null)
            instance = new RepositoryLoader();
        return instance;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = "br.edu.ifba.mybeerapp.repository." + packageName;
    }

    public IRepository getRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".Repository").getConstructor(Context.class);
            IRepository repository = (IRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getRepository();
        }
        return null;
    }

    public IRepository getRepository()
    {
        try {
            return (IRepository) Class.forName(this.packageName + ".Repository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBebidaRepository getBebidaRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".BebidaRepository").getConstructor(Context.class);
            IBebidaRepository repository = (IBebidaRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getBebidaRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getBebidaRepository();
        }
        return null;
    }

    public IBebidaRepository getBebidaRepository()
    {
        try {
            return (IBebidaRepository) Class.forName(this.packageName + ".BebidaRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ILojaRepository getLojaRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".LojaRepository").getConstructor(Context.class);
            ILojaRepository repository = (ILojaRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getLojaRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getLojaRepository();
        }
        return null;
    }

    public ILojaRepository getLojaRepository()
    {
        try {
            return (ILojaRepository) Class.forName(this.packageName + ".LojaRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ICestaRepository getCestaRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".CestaRepository").getConstructor(Context.class);
            ICestaRepository repository = (ICestaRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getCestaRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getCestaRepository();
        }
        return null;
    }

    public ICestaRepository getCestaRepository()
    {
        try {
            return (ICestaRepository) Class.forName(this.packageName + ".CestaRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IMarcaRepository getMarcaRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".MarcaRepository").getConstructor(Context.class);
            IMarcaRepository repository = (IMarcaRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getMarcaRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IMarcaRepository getMarcaRepository()
    {
        try {
            return (IMarcaRepository) Class.forName(this.packageName + ".MarcaRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IModeloRepository getModeloRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".ModeloRepository").getConstructor(Context.class);
            IModeloRepository repository = (IModeloRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getModeloRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getModeloRepository();
        }
        return null;
    }

    public IModeloRepository getModeloRepository()
    {
        try {
            return (IModeloRepository) Class.forName(this.packageName + ".ModeloRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IProdutoRepository getProdutoRepository(Context context)
    {
        try {
            Constructor constructor = Class.forName(this.packageName + ".ProdutoRepository").getConstructor(Context.class);
            IProdutoRepository repository = (IProdutoRepository) constructor.newInstance(context);
            if(repository == null)
                return this.getProdutoRepository();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return this.getProdutoRepository();
        }
        return null;
    }

    public IProdutoRepository getProdutoRepository()
    {
        try {
            return (IProdutoRepository) Class.forName(this.packageName + ".ProdutoRepository").newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}