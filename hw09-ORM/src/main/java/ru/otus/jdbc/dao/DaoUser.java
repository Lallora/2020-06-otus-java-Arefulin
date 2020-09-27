package ru.otus.jdbc.dao;

import ru.otus.core.models.User;
import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public interface DaoUser {

    Optional<User> findById(long id);

    long insertUser(User user);

    boolean update(User user) throws Exception;

    long getMaxNumberOfTableRecords(User user);

    SessionManager getSessionManager();
}
