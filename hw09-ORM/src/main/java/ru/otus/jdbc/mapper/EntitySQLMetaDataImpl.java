package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM %s;";
    private static final String SELECT_ID_TEMPLATE = "SELECT * FROM %s WHERE %s = ?;";
    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s = ?;";
    private static final String COUNT_TEMPLATE = "SELECT COUNT(*) AS TOTAL_COUNT  FROM %s;";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getInsertSql() throws RuntimeException {
        String tableName = entityClassMetaData.getName();
        String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
        String what = "?,".repeat(entityClassMetaData.getFieldsWithoutId().size());
        return String.format(INSERT_TEMPLATE, tableName, fieldsWithoutId, what.substring(0, what.length() - 1));
    }

    @Override
    public String getUpdateSql() throws RuntimeException {
        String tableName = entityClassMetaData.getName();
        String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> f.getName() + " = ?")
                .collect(Collectors.joining(","));
        return String.format(UPDATE_TEMPLATE, tableName, fieldsWithoutId, entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectByIdSql() throws RuntimeException {
        return String.format(SELECT_ID_TEMPLATE, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() throws RuntimeException {
        return String.format(SELECT_ALL_TEMPLATE, entityClassMetaData.getName());
    }
}