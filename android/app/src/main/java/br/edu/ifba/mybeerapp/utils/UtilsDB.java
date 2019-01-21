package br.edu.ifba.mybeerapp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.mybeerapp.exceptions.ColunmTypeNotKnownException;

public class UtilsDB
{
    @NonNull
    public static String generateStringFields(String modelClassName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String fields = "";

        Object object = Class.forName(modelClassName).newInstance();
        for(Field f : object.getClass().getDeclaredFields())
            fields += f.getName() + ",";

        return  fields.substring(0, fields.length() - 1);
    }

    public static ContentValues getContentValues(Object model) throws
            IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        ContentValues contentValues = new ContentValues();

        for(Field f : model.getClass().getDeclaredFields())
        {
            String fieldName = f.getName();
            String methodName = "get" + fieldName.substring(0,1).toUpperCase() +
                    fieldName.substring(1, fieldName.length());

            Method method = model.getClass().getMethod(methodName);

            contentValues.put(fieldName, String.valueOf(method.invoke(model)));
        }

        return contentValues;
    }

    public static List<Object> createListModel(String modelClassName, Cursor dbResult)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            InvocationTargetException, ColunmTypeNotKnownException
    {
        List<Object> models = new ArrayList<>();

        dbResult.moveToFirst();

        Object model;

        while(!dbResult.isAfterLast())
        {
            model = Class.forName(modelClassName).newInstance();

            Method[] methods = model.getClass().getMethods();

            for(Method method : methods)
            {
                String name = method.getName();

                if(name.contains("set"))
                {
                    String typeName = method.getParameterTypes()[0].getTypeName();
                    String fieldName = name.substring(3, name.length()).toLowerCase();

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
                        throw new ColunmTypeNotKnownException(typeName);
                }
            }

            models.add(model);

            dbResult.moveToNext();
        }

        return models;
    }

}
