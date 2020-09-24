package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcDAO;

public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql() throws ExcDAO;

    String getInsertSql();

    String getUpdateSql() throws ExcDAO;
}
