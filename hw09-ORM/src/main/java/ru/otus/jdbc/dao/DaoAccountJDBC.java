package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.ExcUserDao;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.core.models.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DaoAccountJDBC implements IDaoAccount {

    private static final Logger logger = LoggerFactory.getLogger(DaoAccountJDBC.class);

    private final SessionManager sessionManager;
    private final DbExecutor<Account> dbExecutor;

    public DaoAccountJDBC(SessionManager sessionManager, DbExecutor<Account> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<Account> findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), "select no, type, rest from account where no  = ?",
                    id, rs -> {
                        try {
                            if (rs.next()) {
//                                return new User(rs.getLong("no"), rs.getString("type"), rs.getBigDecimal("rest"));
                                return new Account(rs.getLong("no"), rs.getString("type"), rs.getInt("rest"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertAccount(Account account) {
        try {
            List<Object> params = new ArrayList<>();
            params.add(account.getRest());
            params.add(account.getType());
            return dbExecutor.executeInsert(getConnection(), "insert into account(type, rest) values (?,?)",
                    Collections.singletonList(account.getType()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExcUserDao(e);
        }
    }

    @Override
    public void insertOrUpdate(Account account) {
    }

    @Override
    public void update(Account account) {
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

}
