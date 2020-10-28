package ru.otus.core.service;

import org.springframework.stereotype.Service;
import ru.otus.cache.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import java.util.Optional;

@Service
public class CachedDbServiceUserImpl extends DbServiceUserImpl {
    private final HwCache<String, User> cache;

    public CachedDbServiceUserImpl(UserDao userDao, HwCache<String, User> cache) {
        super(userDao);
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        long userId = super.saveUser(user);
        cache.put(String.valueOf(userId), user);
        return userId;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(cache.get(String.valueOf(id)))
                .or(() -> {
                    Optional<User> user = super.getUser(id);
                    user.ifPresent(u -> cache.put(String.valueOf(id), u));
                    return user;
                });
    }
}
