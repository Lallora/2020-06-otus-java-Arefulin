package ru.otus.jdbc.mapper.interfaces;

import ru.otus.core.exception.DAOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor() throws NoSuchMethodException, DAOException;

    Field getIdField() throws DAOException;

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
