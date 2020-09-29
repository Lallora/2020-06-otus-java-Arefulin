package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM %s;";
    private static final String SELECT_ID_TEMPLATE = "SELECT * FROM %s WHERE %s = ?;";
    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s = ?;";
    private static final String COUNT_TEMPLATE = "SELECT COUNT(*) AS TOTAL_COUNT  FROM %s;";
    private String insertSql = "";
    private String updateSql = "";
    private String selectByIdSql = "";
    private String selectAllSql = "";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getInsertSql() throws RuntimeException {
        if (insertSql == "") {
            String tableName = entityClassMetaData.getName();
            String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
            String what = "?,".repeat(entityClassMetaData.getFieldsWithoutId().size());
            insertSql = String.format(INSERT_TEMPLATE, tableName, fieldsWithoutId, what.substring(0, what.length() - 1));
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() throws RuntimeException {
        if (updateSql == "") {
            String tableName = entityClassMetaData.getName();
            String fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                    .map(f -> f.getName() + " = ?")
                    .collect(Collectors.joining(","));
            updateSql = String.format(UPDATE_TEMPLATE, tableName, fieldsWithoutId, entityClassMetaData.getIdField().getName());
        }
        return updateSql;
    }

    @Override
    public String getSelectByIdSql() throws RuntimeException {
        if (selectByIdSql == "") {
            selectByIdSql = String.format(SELECT_ID_TEMPLATE, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
        }
        return selectByIdSql;
    }

    @Override
    public String getSelectAllSql() throws RuntimeException {
        if (selectAllSql == "") {
            selectAllSql = String.format(SELECT_ALL_TEMPLATE, entityClassMetaData.getName());
        }
        return selectAllSql;
    }
}