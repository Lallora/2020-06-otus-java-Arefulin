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
    public void insertUser(User user) {
        long id = jdbcMapper.insert(user);
        if (id == user.getId()) {
            update(user);
        } else {
            user.setId(id);
        }
    }

    @Override
    public void update(User user) {
        jdbcMapper.update(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcMapper.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
