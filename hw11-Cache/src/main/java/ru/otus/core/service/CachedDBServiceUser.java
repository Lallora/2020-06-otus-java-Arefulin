package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.model.User;
import java.util.Optional;

public class CachedDBServiceUser implements DBServiceUser {

    private final DBServiceUser dbServiceUser;
    private final HwCache<String, User> cache;

    public CachedDBServiceUser(DBServiceUser dbServiceUser, HwCache<String, User> cache) {
        this.dbServiceUser = dbServiceUser;
        this.cache = cache;
    }

    private static final Logger logger = LoggerFactory.getLogger(CachedDBServiceUser.class);

    @Override
    public long saveUser(User user) {
        long userId = this.dbServiceUser.saveUser(user);
        cache.put(Long.toString(userId), user);
        logger.info("created user: {}", userId);
        return userId;
    }


    @Override
    public Optional<User> getUser(long id) {
        Optional<User> value = Optional.ofNullable(cache.get(Long.toString(id)));
        if(value.isEmpty()) {
           value = this.dbServiceUser.getUser(id);
        }
        return value;
    }
}
