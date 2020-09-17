package ru.otus.jdbc.mapper.interfaces;

import ru.otus.core.exception.DAOException;

public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql() throws DAOException;

    String getInsertSql();

    String getUpdateSql() throws DAOException;
}
