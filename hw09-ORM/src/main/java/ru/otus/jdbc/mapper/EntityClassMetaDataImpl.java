package ru.otus.jdbc.mapper;

import ru.otus.core.exception.DAOException;
import ru.otus.jdbc.annotations.Id;
import ru.otus.jdbc.mapper.interfaces.EntityClassMetaData;

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
    public String getName() {
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
        List<Field> list = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class)).collect(Collectors.toList());
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new DAOException("No field with annotation @Id in the class");
        }
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
