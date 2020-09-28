package ru.otus.jdbc.dao;

import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.core.models.Account;

import java.util.Optional;

public interface DaoAccount {
    Optional<Account> findById(long id);

    void insertAccount(Account account);

    void insertOrUpdate(Account account);

    void update(Account account);

    SessionManager getSessionManager();
}
