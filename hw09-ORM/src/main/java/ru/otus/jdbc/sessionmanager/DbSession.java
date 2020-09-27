package ru.otus.jdbc.sessionmanager;

import java.sql.Connection;

public class DbSession implements DatabaseSession {

    private final Connection connection;

    DbSession(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
