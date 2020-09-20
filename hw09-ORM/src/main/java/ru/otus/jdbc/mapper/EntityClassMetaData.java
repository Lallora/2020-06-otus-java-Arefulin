package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcDAO;
import ru.otus.exceptions.ExcHaveNotIdField;
import ru.otus.exceptions.ExcIdFieldMustBeOnlyOne;
import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaData<T> implements IEntityClassMetaData<T> {

    private final Class<T> clazz;

    public EntityClassMetaData(Class<T> clazz) {
        this.clazz = clazz;
        getIdField();
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() throws ExcDAO {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new ExcDAO("No constructor in the class");
        }
    }

    @Override
    public Field getIdField() {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> idAnnotated = new LinkedList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                idAnnotated.add(field);
            }
        }

        if (idAnnotated.size() > 1) {
            throw new ExcIdFieldMustBeOnlyOne();
        }

        if (idAnnotated.isEmpty()) {
            throw new ExcHaveNotIdField(clazz + " have no Id annotated field");
        }

        return idAnnotated.get(0);
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
