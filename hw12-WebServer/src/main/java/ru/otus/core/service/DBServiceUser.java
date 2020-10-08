package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    User getUserByLogin(String login);

    List<User> getAllUsers();

}
