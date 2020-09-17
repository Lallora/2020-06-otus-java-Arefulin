package ru.otus.core.dao.impl;

import ru.otus.core.dao.UserDao;
import ru.otus.core.exception.DAOException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.mapper.interfaces.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private final JdbcMapper<User> jdbcUserMapper;

    public UserDaoImpl(SessionManagerJdbc sessionManager) {
        this.jdbcUserMapper = new JdbcMapperImpl(sessionManager);
    }


    @Override
    public Optional<User> findById(long id) {
        User user = jdbcUserMapper.findById(id, User.class);
        Optional<User> opt = Optional.ofNullable(user);
        return opt;
    }

    @Override
    public long insertUser(User user) {
        jdbcUserMapper.insert(user);
        return 1;
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcUserMapper.insertOrUpdate(user);
    }

    @Override
    public void update(User user) {
        try {
            jdbcUserMapper.update(user);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }
}
