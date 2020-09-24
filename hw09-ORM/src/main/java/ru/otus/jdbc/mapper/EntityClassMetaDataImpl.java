package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;
import ru.otus.exceptions.ExcDAO;
import ru.otus.exceptions.ExcMetadataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(String name, Constructor<T> constructor, Field idField, List<Field> allFields) {
        this.name = name;
        this.constructor = constructor;
        this.idField = idField;
        this.allFields = allFields;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() throws ExcDAO {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId == null ?
                fieldsWithoutId = allFields.stream()
                        .filter(Predicate.isEqual(idField).negate())
                        .collect(Collectors.toUnmodifiableList()) :
                fieldsWithoutId;
    }

    public static <T> EntityClassMetaDataImpl<T> create(Class<T> type) {
        var allFields = Arrays.stream(type.getDeclaredFields())
                .filter(EntityClassMetaDataImpl::isApplicableField)
                .collect(Collectors.toUnmodifiableList());
        var idField = allFields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new ExcMetadataException("No @Id field for type " + type.getName()));
        if (!isInteger(idField.getType())) {
            throw new ExcMetadataException("Non-numeric @Id field type: " + idField.getType());
        }
        Constructor<T> constructor = null;
        try {
            constructor = type.getConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExcMetadataException("Cannot find no-args constructor for " + type.getName(), e);
        }

        allFields.forEach(field -> field.setAccessible(true));

        return new EntityClassMetaDataImpl<>(type.getSimpleName(), constructor, idField, allFields);

    }

    public static boolean isInteger(Class<?> type) {
        if (!type.isPrimitive()) {
            if (!Number.class.isAssignableFrom(type)) {
                return false;
            } else {
                try {
                    type = (Class<?>) type.getDeclaredField("TYPE").get(null);
                } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
                    return false;
                }
            }
        }
        return allowedIdTypes.contains(type);
    }

    private static final Set<Class<? extends Number>> allowedIdTypes = Stream.of(int.class, long.class, short.class,
            byte.class).collect(Collectors.toSet());

    private static boolean isApplicableField(Field field) {
        var modifier = field.getModifiers();
        return !Modifier.isStatic(modifier);
    }

}
