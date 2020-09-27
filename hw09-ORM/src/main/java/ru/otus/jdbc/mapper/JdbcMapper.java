package ru.otus.jdbc.mapper;

import ru.otus.exceptions.DAOException;

import java.util.Optional;

public interface JdbcMapper<T> {

    Optional<T> findById(long id) throws DAOException;

    long insert(T objectData) throws DAOException;

    boolean update(T objectData, long id) throws Exception;

    long getMaxNumberOfTableRecords(T objectData) throws DAOException;
}
