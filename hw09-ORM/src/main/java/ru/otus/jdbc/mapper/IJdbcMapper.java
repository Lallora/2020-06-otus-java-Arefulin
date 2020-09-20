package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcDAO;

public interface IJdbcMapper<T> {

    T findById(long id, Class<T> clazz);

    void insert(T objectData);

    void update(T objectData) throws ExcDAO;

    void insertOrUpdate(T objectData);
}
