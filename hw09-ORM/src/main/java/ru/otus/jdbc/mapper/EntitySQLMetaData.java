package ru.otus.jdbc.mapper;

public interface EntitySQLMetaData {

    String getInsertSql();

    String getUpdateSql();

    String getSelectByIdSql();

    String getSelectAllSql();
}
