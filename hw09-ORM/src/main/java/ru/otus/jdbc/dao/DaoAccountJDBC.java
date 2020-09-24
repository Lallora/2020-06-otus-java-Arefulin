package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.jdbc.sessionmanager.SessionManagerImpl;

import java.util.Optional;

public class DaoAccountJDBC implements DaoAccount {

    private static final Logger logger = LoggerFactory.getLogger(DaoAccountJDBC.class);

    private final SessionManagerImpl sessionManagerImpl;
    private final JdbcMapperImpl<Account> jdbcMapperImpl;

    public DaoAccountJDBC(SessionManagerImpl sessionManagerImpl, JdbcMapperImpl<Account> jdbcMapperImpl) {
        this.sessionManagerImpl = sessionManagerImpl;
        this.jdbcMapperImpl = jdbcMapperImpl;
    }

    @Override
    public Optional<Account> findById(long id) {
        return jdbcMapperImpl.findById(id);
    }

    @Override
    public long insertAccount(Account account) {
        jdbcMapperImpl.insert(account);
        return account.getNo();
    }

    @Override
    public void insertOrUpdate(Account account) {
        jdbcMapperImpl.insertOrUpdate(account);
    }

    @Override
    public void update(Account account) {
        jdbcMapperImpl.update(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManagerImpl;
    }

}
