package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private String name = "";
    private Constructor<T> constructor = null;
    private Field idField = null;
    private List<Field> fieldsWithoutId = null;
    private List<Field> fields = null;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() throws RuntimeException {
        if (name == "") {
            name = clazz.getSimpleName();
        }
        return name;
    }

    @Override
    public Constructor<T> getConstructor() throws RuntimeException {
        if (constructor == null) {
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No constructor in the class");
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() throws RuntimeException {
        if (idField == null) {
            List<Field> list;
            list = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .collect(Collectors.toList());
            for (Field field : list) {
                field.setAccessible(true);
            }
            if (list.size() > 0) {
                idField = list.get(0);
            } else {
                throw new RuntimeException("No field with annotation @Id in the class");
            }
        }
        return idField;
    }

    @Override
    public List<Field> getFieldsWithoutId() throws RuntimeException {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(Id.class))
                    .collect(Collectors.toList());
        }
        return fieldsWithoutId;
    }

    @Override
    public List<Field> getAllFields() throws RuntimeException {
        if (fields == null) {
            fields = Arrays.stream(clazz.getDeclaredFields())
                    .collect(Collectors.toList());
        }
        return fields;
    }
}
