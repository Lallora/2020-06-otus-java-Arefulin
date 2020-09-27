package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.User;
import ru.otus.exceptions.ExcDbService;
import ru.otus.jdbc.dao.DaoUser;

import java.util.Optional;

public class DbServiceUserImpl implements DbServiceUser {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);
    private long userId = 0;
    private final DaoUser daoUser;

    public DbServiceUserImpl(DaoUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = daoUser.getSessionManager()) {
            sessionManager.beginSession();
            long maxId = daoUser.getMaxNumberOfTableRecords(user);
            long curId = user.getId();
            try {
               switch ((curId == 0) ? 0 : (1 <= curId && curId <= maxId) ? 1 : (curId > maxId) ? 2 : 3) {
                    case 0: {
                        logger.info("need to insert user, name: " + user.getName() + ", age: " + user.getAge());
                        userId = daoUser.insertUser(user);
                        sessionManager.commitSession();
                        logger.info("INSERTED user id: " + userId + ", name: " + user.getName() + ", age: " + user.getAge());
                        return userId;
                    }
                    case 1: {
                        logger.info("need to update user id: " + curId + ", name: " + user.getName() + ", age: " + user.getAge());
                        boolean result = daoUser.update(user);
                        if (result) userId = 0;
                        else userId = -1;
                        sessionManager.commitSession();
                        logger.info("       UPDATED user id: " + curId + ", name: " + user.getName() + ", age: " + user.getAge());
                        return curId;
                    }
                    case 2: {
                        logger.info("need to insert or to update user id: " + curId + ", name: " + user.getName() + ", age: " + user.getAge());
                        userId = daoUser.insertUser(user);
                        sessionManager.commitSession();
                        logger.info("        INSERTED_OR_UPDATED user id: " + userId + ", name: " + user.getName() + ", age: " + user.getAge());
                        return userId;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + user.getId());
                }
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
