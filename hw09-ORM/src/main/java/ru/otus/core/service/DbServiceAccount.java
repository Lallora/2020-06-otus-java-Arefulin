package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.IDaoAccount;

import java.util.Optional;

public class DbServiceAccount implements IDBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUser.class);

    private final IDaoAccount daoAccount;

    public DbServiceAccount(IDaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = daoAccount.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = daoAccount.insertAccount(account);
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new ExcDbService(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long id) {
        try (var sessionManager = daoAccount.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> accountOptional = daoAccount.findById(id);

                logger.info("Account: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
