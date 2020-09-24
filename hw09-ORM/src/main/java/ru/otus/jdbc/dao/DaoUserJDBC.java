package ru.otus.jdbc.dao;

import ru.otus.core.models.User;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.jdbc.sessionmanager.SessionManagerImpl;

import java.util.Optional;

public class DaoUserJDBC implements DaoUser {

    private final SessionManagerImpl sessionManagerImpl;
    private final JdbcMapperImpl<User> jdbcMapperImpl;

    public DaoUserJDBC(SessionManagerImpl sessionManagerImpl, JdbcMapperImpl<User> jdbcMapperImpl) {
        this.sessionManagerImpl = sessionManagerImpl;
        this.jdbcMapperImpl = jdbcMapperImpl;
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcMapperImpl.findById(id);
    }

    @Override
    public long insertUser(User user) {
        jdbcMapperImpl.insert(user);
        return user.getId();
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcMapperImpl.insertOrUpdate(user);
    }

    @Override
    public void update(User user) {
        jdbcMapperImpl.update(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManagerImpl;
    }

}
