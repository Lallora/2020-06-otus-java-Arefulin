package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.User;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.DaoUser;

import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DaoUser daoUser;

    public DbServiceUserImpl(DaoUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public void saveUser(User user) {
        try (var sessionManager = daoUser.getSessionManager()) {
            logger.info("sent user id: " + user.getId() + ". name: " + user.getName() + ", age: " + user.getAge());
            sessionManager.beginSession();
            try {
                User testUser = daoUser.findById(user.getId()).orElse(null);
                if (testUser != null) {
                    daoUser.update(user);
                    logger.info("updated user id: " + user.getId() + ". name: " + user.getName() + ", age: " + user.getAge());
                } else {
                    daoUser.insertUser(user);
                    logger.info("got user id: " + user.getId() + ". name: " + user.getName() + ", age: " + user.getAge());
                }
                sessionManager.commitSession();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new ExcDbService(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (var sessionManager = daoUser.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = daoUser.findById(id);
                logger.info("gotten user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
        }
        return Optional.empty();
    }
}
