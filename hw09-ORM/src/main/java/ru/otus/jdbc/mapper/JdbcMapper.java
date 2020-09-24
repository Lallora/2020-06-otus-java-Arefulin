package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcDAO;

import java.util.Optional;

public interface JdbcMapper<T> {

    Optional<T> findById(long id);

    void insert(T objectData);

    void update(T objectData) throws ExcDAO;

    void insertOrUpdate(T objectData);
}
