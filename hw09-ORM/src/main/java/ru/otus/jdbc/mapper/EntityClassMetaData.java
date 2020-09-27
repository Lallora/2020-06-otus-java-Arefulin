package ru.otus.jdbc.mapper;

import ru.otus.exceptions.DAOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public interface EntityClassMetaData<T> {

    String getName() throws DAOException;

    Constructor<T> getConstructor() throws DAOException;

    Field getIdField() throws DAOException;

    List<Field> getFieldsWithoutId() throws DAOException;

    List<Field> getAllFields() throws DAOException;
}
