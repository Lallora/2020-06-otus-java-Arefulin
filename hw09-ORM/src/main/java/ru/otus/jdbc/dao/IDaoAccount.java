package ru.otus.jdbc.dao;

import ru.otus.jdbc.sessionmanager.ISessionManager;
import ru.otus.core.models.Account;

import java.util.Optional;

public interface IDaoAccount {
    Optional<Account> findById(long id);

    long insertAccount(Account account);

    void insertOrUpdate(Account account);

    void update(Account account);

    ISessionManager getSessionManager();
}
