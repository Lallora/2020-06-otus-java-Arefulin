package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public class DaoAccountJDBC implements DaoAccount {

    private static final Logger logger = LoggerFactory.getLogger(DaoAccountJDBC.class);

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
    public void insertAccount(Account account) {
        long id = jdbcMapper.insert(account);
        if (id == account.getNo()) {
            update(account);
        } else {
        account.setNo(id);
        }
    }

    @Override
    public void update(Account account){
        jdbcMapper.update(account);
    }

    @Override
    public void insertOrUpdate(Account account) {
        jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
