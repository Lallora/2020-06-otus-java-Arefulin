package ru.otus.jdbc.sessionmanager;

public interface ISessionManager extends AutoCloseable {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    IDatabaseSession getCurrentSession();
}
