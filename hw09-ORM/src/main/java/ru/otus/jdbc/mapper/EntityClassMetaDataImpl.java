package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;

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
    public String getName() throws RuntimeException {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() throws RuntimeException {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor in the class");
        }
    }

    @Override
    public Field getIdField() throws RuntimeException {
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
            throw new RuntimeException("No field with annotation @Id in the class");
        }
    }

    @Override
    public List<Field> getFieldsWithoutId() throws RuntimeException {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getAllFields() throws RuntimeException {
        return Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toList());
    }
}
