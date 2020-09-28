package ru.otus.jdbc.dao;

import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.core.models.User;

import java.util.Optional;

public interface DaoUser {

    Optional<User> findById(long id);

    void insertUser(User user);

    void update(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
