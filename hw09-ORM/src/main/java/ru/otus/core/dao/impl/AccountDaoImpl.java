package ru.otus.core.dao.impl;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.exception.DAOException;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.mapper.interfaces.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoImpl implements AccountDao {
    private final JdbcMapper<Account> jdbcAccountMapper;

    public AccountDaoImpl(SessionManagerJdbc sessionManager) {
        this.jdbcAccountMapper = new JdbcMapperImpl(sessionManager);
    }

    @Override
    public Optional<Account> findById(long id) {
        Account account = jdbcAccountMapper.findById(id, Account.class);
        Optional<Account> opt = Optional.ofNullable(account);
        return opt;
    }

    @Override
    public long insertAccount(Account account) {
        jdbcAccountMapper.insert(account);
        return 1;
    }

    @Override
    public void insertOrUpdate(Account account) {
        jdbcAccountMapper.insertOrUpdate(account);
    }

    @Override
    public void update(Account account) {
        try {
            jdbcAccountMapper.update(account);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }
}
