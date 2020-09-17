package ru.otus.jdbc.mapper;

import ru.otus.core.exception.DAOException;
import ru.otus.jdbc.mapper.interfaces.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaDataImpl<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaDataImpl<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() throws DAOException {
        return String.format("SELECT * FROM %s WHERE %s = ?", entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String tableName = entityClassMetaData.getName();
        String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
        String what = "?,".repeat(entityClassMetaData.getFieldsWithoutId().size());
        return String.format("INSERT INTO %s(%s) VALUES (%s)", tableName, fieldsWithoutId, what.substring(0, what.length() - 1));
    }

    @Override
    public String getUpdateSql() throws DAOException {
        String tableName = entityClassMetaData.getName();
        String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> f.getName() + " = ?")
                .collect(Collectors.joining(","));
        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, fieldsWithoutId, entityClassMetaData.getIdField().getName());
    }
}
