package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.ExcUserDao;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.ISessionManager;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.core.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoUserJDBC implements IDaoUser {
    private static final Logger logger = LoggerFactory.getLogger(DaoUserJDBC.class);

    private final SessionManager sessionManager;
    private final DbExecutor<User> dbExecutor;
    //private EntitySQLMetaData<String> sqlGenerator;


    public DaoUserJDBC(SessionManager sessionManager, DbExecutor<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), "select id, name, age from user where id  = ?",
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return new User(rs.getLong("id"), rs.getString("name"), rs.getInt("age"));
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
    public long insertUser(User user) {
        try {
            List<Object> params = new ArrayList<>();
            params.add(user.getName());
            params.add(user.getAge());
            return dbExecutor.executeInsert(getConnection(),
                    "insert into user(name, age) values (?,?)", params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExcUserDao(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
    }

    @Override
    public void update(User user) {

    }

    @Override
    public ISessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
