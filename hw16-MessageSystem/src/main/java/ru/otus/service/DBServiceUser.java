package ru.otus.service;

import ru.otus.model.User;

import java.util.Collection;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    void updateUser(User user);

    Collection<User> getAll();

    Optional<User> getRandom();
}
