package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.DaoAccount;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DaoAccount daoAccount;

    public DbServiceAccountImpl(DaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = daoAccount.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = daoAccount.insertAccount(account);
                sessionManager.commitSession();
                logger.info("created account id: " + account.getNo() + ", type: " + account.getType() + ", rest: " + account.getRest());
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
                logger.info("gotten account: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
