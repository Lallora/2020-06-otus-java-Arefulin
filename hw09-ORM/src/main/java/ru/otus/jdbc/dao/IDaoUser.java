package ru.otus.jdbc.dao;

import ru.otus.jdbc.sessionmanager.ISessionManager;
import ru.otus.core.models.User;

import java.util.Optional;

public interface IDaoUser {
    Optional<User> findById(long id);

    long insertUser(User user);

    void insertOrUpdate(User user);

    void update(User user);

    ISessionManager getSessionManager();
}
