package ru.otus.jdbc.dao;


import ru.otus.core.models.Account;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public class DaoAccountJDBC implements DaoAccount {

    private final SessionManager sessionManager;

    private final JdbcMapper<Account> jdbcMapper;

    public DaoAccountJDBC(SessionManager sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        return jdbcMapper.findById(id);
    }

    @Override
    public long insertAccount(Account account) {
        long id = jdbcMapper.insert(account);
        account.setNo(id);
        return id;
    }

    @Override
    public boolean update(Account account) throws Exception {
        return jdbcMapper.update(account, account.getNo());
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public long getMaxNumberOfTableRecords(Account account) {
        return jdbcMapper.getMaxNumberOfTableRecords(account);
    }
}
