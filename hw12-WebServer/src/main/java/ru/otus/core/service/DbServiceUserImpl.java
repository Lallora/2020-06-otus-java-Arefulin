package ru.otus.core.service;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        User user = null;
        try {
            SessionManager currentSession = userDao.getSessionManager();
            Session session = currentSession.getSession();
//            users = session.createNativeQuery("SELECT * FROM users WHERE ", User.class).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            SessionManager currentSession = userDao.getSessionManager();
            Session session = currentSession.getSession();
            users = session.createNativeQuery("SELECT * FROM users", User.class).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return users;
    }
}
