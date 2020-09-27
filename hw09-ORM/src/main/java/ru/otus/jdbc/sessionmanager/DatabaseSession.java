package ru.otus.jdbc.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {
    Connection getConnection();
}
