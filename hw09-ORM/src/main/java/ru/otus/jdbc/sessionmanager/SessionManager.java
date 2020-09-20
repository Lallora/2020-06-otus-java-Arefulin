package ru.otus.jdbc.sessionmanager;


import ru.otus.exceptions.ExcSessionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SessionManager implements ISessionManager {

    private static final int TIMEOUT_IN_SECONDS = 5;
    private final DataSource dataSource;
    private Connection connection;
    private DbSession dbSession;

    public SessionManager(DataSource dataSource) {
        if (dataSource == null) {
            throw new ExcSessionManager("Datasource is null");
        }
        this.dataSource = dataSource;
    }

    @Override
    public void beginSession() {
        try {
            connection = dataSource.getConnection();
            dbSession = new DbSession(connection);
        } catch (SQLException e) {
            throw new ExcSessionManager(e);
        }
    }

    @Override
    public void commitSession() {
        checkConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new ExcSessionManager(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new ExcSessionManager(e);
        }
    }

    @Override
    public void close() {
        checkConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ExcSessionManager(e);
        }
    }

    @Override
    public DbSession getCurrentSession() {
        checkConnection();
        return dbSession;
    }

    private void checkConnection() {
        try {
            if (connection == null || !connection.isValid(TIMEOUT_IN_SECONDS)) {
                throw new ExcSessionManager("Connection is invalid");
            }
        } catch (SQLException ex) {
            throw new ExcSessionManager(ex);
        }
    }
}
