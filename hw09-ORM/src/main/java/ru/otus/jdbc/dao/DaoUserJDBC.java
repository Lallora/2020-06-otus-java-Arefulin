package ru.otus.jdbc.dao;

import ru.otus.core.models.User;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public class DaoUserJDBC implements DaoUser {

    private final SessionManager sessionManager;

    private final JdbcMapper<User> jdbcMapper;

    public DaoUserJDBC(SessionManager sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcMapper.findById(id);
    }

    @Override
    public long insertUser(User user) {
        long id = jdbcMapper.insert(user);
        user.setId(id);
        return id;
    }

    @Override
    public boolean update(User user) throws Exception {
        return jdbcMapper.update(user, user.getId());
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public long getMaxNumberOfTableRecords(User user) {
        return jdbcMapper.getMaxNumberOfTableRecords(user);
    }
}
