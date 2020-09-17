package ru.otus.core.dao;

import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount(Account account);

    void insertOrUpdate(Account account);

    void update(Account account);

    SessionManager getSessionManager();
}
