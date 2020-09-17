package ru.otus.jdbc.mapper.interfaces;

import ru.otus.core.exception.DAOException;

public interface JdbcMapper<T> {
    void insert(T objectData);

    void update(T objectData) throws DAOException;

    void insertOrUpdate(T objectData);

    T findById(long id, Class<T> clazz);
}
