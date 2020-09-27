package ru.otus.core.service;

import ru.otus.core.models.User;
import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
