package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.User;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.IDaoUser;

import java.util.Optional;

public class DbServiceUser implements IDBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUser.class);

    private final IDaoUser daoUser;

    public DbServiceUser(IDaoUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = daoUser.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = daoUser.insertUser(user);
                sessionManager.commitSession();

                logger.info("created user id: " + userId + ", name: " + user.getName() + ", age: " + user.getAge());
                return userId;
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
            return Optional.empty();
        }
    }
}
