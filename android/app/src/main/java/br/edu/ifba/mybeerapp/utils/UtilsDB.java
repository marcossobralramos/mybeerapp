package br.edu.ifba.mybeerapp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.ifba.mybeerapp.model.annotations.RepositoryNotAccess;
import br.edu.ifba.mybeerapp.model.interfaces.IModel;
import br.edu.ifba.mybeerapp.repository.Repository;

public class UtilsDB
{
    public static String PACKAGE_NAME_MODEL = "br.edu.ifba.mybeerapp.model";
    @NonNull
    public static String generateStringFields(String modelClassName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String fields = "";

        Object object = Class.forName(modelClassName).newInstance();
        for(Field f : object.getClass().getDeclaredFields())
        {
            String typeName = f.getType().getTypeName();

            if(Map.class.getName().equals(typeName) ||
                    ArrayList.class.getName().equals(typeName))
                continue;

            String modelName = f.getName().substring(0,1).toUpperCase() +
                    f.getName().substring(1,f.getName().length());

            try
            {
                if(Class.forName(UtilsDB.PACKAGE_NAME_MODEL + "." + modelName) != null)
                    fields += f.getName() + "Id,";
            }
            catch (ClassNotFoundException ex)
            {
                fields += f.getName() + ",";
            }
        }
        return  fields.substring(0, fields.length() - 1);
    }

    public static ContentValues getContentValues(Object model) throws
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ContentValues contentValues = new ContentValues();

        for(Field f : model.getClass().getDeclaredFields())
        {
            String fieldName = f.getName();
            String methodName = "get" + fieldName.substring(0,1).toUpperCase() +
                    fieldName.substring(1, fieldName.length());

            Method method = model.getClass().getMethod(methodName);

            if(method.isAnnotationPresent(RepositoryNotAccess.class))
                continue;

            String typeName = method.getReturnType().getTypeName();

            String modelName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());

            try
            {
                if(Class.forName(UtilsDB.PACKAGE_NAME_MODEL + "." + modelName) != null)
                    fieldName += "Id";
            }
            catch (ClassNotFoundException ex) {}

            contentValues.put(fieldName, String.valueOf(method.invoke(model)));
        }

        return contentValues;
    }

    public static List<IModel> createListModel(String modelClassName, Cursor dbResult, Repository repository)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException
    {
        List<IModel> models = new ArrayList<>();

        dbResult.moveToFirst();

        IModel model;

        while(!dbResult.isAfterLast())
        {
            model = (IModel) Class.forName(modelClassName).newInstance();

            Method[] methods = model.getClass().getMethods();

            for(Method method : methods)
            {
                if(method.isAnnotationPresent(RepositoryNotAccess.class))
                    continue;

                String name = method.getName();

                if(name.contains("set"))
                {
                    String typeName = method.getParameterTypes()[0].getTypeName();

                    String fieldName = name.substring(3, 4).toLowerCase() +
                            name.substring(4, name.length());

                    if(String.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getString(dbResult.getColumnIndex(fieldName)));
                    else if(Integer.class.getName().equals(typeName) || int.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getInt(dbResult.getColumnIndex(fieldName)));
                    else if(Double.class.getName().equals(typeName) || double.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getDouble(dbResult.getColumnIndex(fieldName)));
                    else if(Blob.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getBlob(dbResult.getColumnIndex(fieldName)));
                    else if(Float.class.getName().equals(typeName) || float.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getFloat(dbResult.getColumnIndex(fieldName)));
                    else if(Long.class.getName().equals(typeName) || long.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getFloat(dbResult.getColumnIndex(fieldName)));
                    else if(Short.class.getName().equals(typeName) || short.class.getName().equals(typeName))
                        method.invoke(model, dbResult.getShort(dbResult.getColumnIndex(fieldName)));
                    else
                    {
                        if(repository != null)
                        {
                            fieldName += "Id";
                            Object obj =  repository.retrieveModel(
                                typeName,
                                String.valueOf(dbResult.getInt(dbResult.getColumnIndex(fieldName)))
                            );
                            method.invoke(model, obj);
                        }
                    }

                }
            }

            models.add(model);

            dbResult.moveToNext();
        }

        return models;
    }

}
