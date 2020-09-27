package ru.otus.jdbc.mapper;

import ru.otus.exceptions.DAOException;

public interface EntitySQLMetaData {

    String getInsertSql() throws DAOException;

    String getUpdateSql() throws Exception;

    String getSelectByIdSql() throws DAOException;

    String getSelectAllSql() throws DAOException;

    String getMaxNumberOfRecords() throws DAOException;

}
