package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaData<T> implements IEntitySQLMetaData {

    public EntityClassMetaData<T> getClassMetaData() {
        return classMetaData;
    }

    private final EntityClassMetaData<T> classMetaData;
    private final static String selectAllSqlTemplate = "SELECT %s FROM %s;";
    private final static String selectByIdSqlTemplate = "SELECT %s FROM %s WHERE %s = ?;";
    private final static String insertSqlTemplate = "INSERT INTO %s (%s) VALUES (%s);";
    private final static String updateSqlTemplate = "UPDATE %s SET %s WHERE %s = ?;";
    private final static String questionJDBCPlaceholder = "?";
    private final static String sqlFieldAndValuesSeparator = ", ";

    public EntitySQLMetaData(Class<T> clazz) {
        classMetaData = new EntityClassMetaData<>(clazz);
    }

    @Override
    public String getSelectAllSql() {
        return String.format(selectAllSqlTemplate, getColumnNamesCommaSeparated(), classMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                selectByIdSqlTemplate,
                getColumnNamesCommaSeparated(),
                classMetaData.getName(),
                classMetaData.getIdField().getName()
        );
    }

    @Override
    public String getInsertSql() {
        String tableName = classMetaData.getName();
        List<Field> fields = classMetaData.getFieldsWithoutId();
        List<String> columnNames = fields.stream().map(o -> o.getName()).collect(Collectors.toList());
        String columnsFiller = String.join(sqlFieldAndValuesSeparator, columnNames);
        String questions = Collections.nCopies(columnNames.size(), questionJDBCPlaceholder)
                .stream()
                .collect(Collectors.joining(sqlFieldAndValuesSeparator));

        return String.format(insertSqlTemplate, tableName, columnsFiller, questions);
    }

    @Override
    public String getUpdateSql() {
        String tableName = classMetaData.getName();
        List<Field> fields = classMetaData.getFieldsWithoutId();
        String nameAssignQuestionPlaceholder = fields
                .stream()
                .map(o -> o.getName() + " = " + questionJDBCPlaceholder)
                .collect(Collectors.joining(sqlFieldAndValuesSeparator));

        return String.format(
                updateSqlTemplate,
                tableName,
                nameAssignQuestionPlaceholder,
                classMetaData.getIdField().getName());
    }

    String getColumnNamesCommaSeparated() {
        return classMetaData.getAllFields()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(sqlFieldAndValuesSeparator));
    }
}
