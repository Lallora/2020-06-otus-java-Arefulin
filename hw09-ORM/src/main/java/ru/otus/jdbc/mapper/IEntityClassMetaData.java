package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcDAO;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public interface IEntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor() throws NoSuchMethodException, ExcDAO;

    Field getIdField() throws ExcDAO;

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
