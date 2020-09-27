package ru.otus.jdbc.dao;

import ru.otus.core.models.Account;
import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public interface DaoAccount {

    Optional<Account> findById(long id);

    long insertAccount(Account account);

    boolean update(Account account) throws Exception;

    long getMaxNumberOfTableRecords(Account account);

    SessionManager getSessionManager();
}
