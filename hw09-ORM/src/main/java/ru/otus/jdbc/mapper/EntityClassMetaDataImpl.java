package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;
import ru.otus.exceptions.DAOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() throws DAOException {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() throws DAOException {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DAOException("No constructor in the class");
        }
    }

    @Override
    public Field getIdField() throws DAOException {
        List<Field> list;
        list = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
        for (Field field : list) {
            field.setAccessible(true);
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new DAOException("No field with annotation @Id in the class");
        }
    }

    @Override
    public List<Field> getFieldsWithoutId() throws DAOException {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getAllFields() throws DAOException {
        return Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toList());
    }
}
