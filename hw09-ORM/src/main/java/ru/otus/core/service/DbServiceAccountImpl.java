package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.DaoAccount;

import java.util.Optional;

public class DbServiceAccountImpl implements DbServiceAccount {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private long accountId = 0;

    private final DaoAccount daoAccount;

    public DbServiceAccountImpl(DaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = daoAccount.getSessionManager()) {
            sessionManager.beginSession();
            long maxId = daoAccount.getMaxNumberOfTableRecords(account);
            long curId = account.getNo();
            try {
                switch ((curId == 0) ? 0 : (1 <= curId && curId <= maxId) ? 1 : (curId > maxId) ? 2 : 3) {
                    case 0: {
                        logger.info("need to insert account, name: " + account.getType() + ", age: " + account.getRest());
                        accountId = daoAccount.insertAccount(account);
                        sessionManager.commitSession();
                        logger.info("INSERTED account no: " + accountId + ", name: " + account.getType() + ", age: " + account.getRest());
                        return accountId;
                    }
                    case 1: {
                        logger.info("need to update account no: " + curId + ", name: " + account.getType() + ", age: " + account.getRest());
                        boolean result = daoAccount.update(account);
                        if (result) accountId = 0;
                        else accountId = -1;
                        sessionManager.commitSession();
                        logger.info("       UPDATED account no: " + curId + ", name: " + account.getType() + ", age: " + account.getRest());
                        return curId;
                    }
                    case 2: {
                        logger.info("need to insert or to update account no: " + curId + ", name: " + account.getType() + ", age: " + account.getRest());
                        accountId = daoAccount.insertAccount(account);
                        sessionManager.commitSession();
                        logger.info("        INSERTED_OR_UPDATED account no: " + accountId + ", name: " + account.getType() + ", age: " + account.getRest());
                        return accountId;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + account.getNo());
                }
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
        }
        return Optional.empty();
    }
}
