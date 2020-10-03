package ru.otus.jdbc.mapper;

import java.util.Optional;

public interface JdbcMapper<T> {

    Optional<T> findById(long id);

    long insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);
}
