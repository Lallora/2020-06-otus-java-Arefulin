package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private static final String SELECT_TEMPLATE = "SELECT * FROM %s %s;";
    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String UPDATE_TEMPLATE = "UPDATE %s SET %s %s;";

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(String selectAllSql, String selectByIdSql, String insertSql, String updateSql) {
        this.selectAllSql = selectAllSql;
        this.selectByIdSql = selectByIdSql;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
    }

    public static EntitySQLMetaDataImpl createFromClass(EntityClassMetaDataImpl<?> classMetaData) {
        var tableName = classMetaData.getName();
        var fields = classMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList());
        return create(tableName, classMetaData.getIdField().getName(), fields);
    }

    public static EntitySQLMetaDataImpl create(String tableName, String idField, List<String> fieldNames) {
        var whereId = String.format("WHERE `%s`=?", idField);

        var selectAll = String.format(SELECT_TEMPLATE, tableName, "");
        var selectById = String.format(SELECT_TEMPLATE, tableName, whereId);
        var insert = String.format(INSERT_TEMPLATE, tableName, String.join(",", fieldNames),
                generateParameters(fieldNames.size()));
        var update = String.format(UPDATE_TEMPLATE, tableName, generateFieldNamesAndParameters(fieldNames), whereId);

        return new EntitySQLMetaDataImpl(selectAll, selectById, insert, update);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() throws RuntimeException {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() throws RuntimeException {
        return updateSql;
    }

    private static String generateFieldNamesAndParameters(List<String> fields) {
        return fields.stream().map(field -> String.format("`%s`=?", field)).collect(Collectors.joining(","));
    }

    private static String generateParameters(int amount) {
        return IntStream.range(0, amount).mapToObj(i -> "?").collect(Collectors.joining(","));
    }
}
