package ru.otus.core.service;

import ru.otus.core.models.Account;

import java.util.Optional;

public interface IDBServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long id);
}
