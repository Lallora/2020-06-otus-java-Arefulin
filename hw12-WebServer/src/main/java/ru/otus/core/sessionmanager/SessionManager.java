package ru.otus.core.sessionmanager;

import org.hibernate.Session;

public interface SessionManager extends AutoCloseable {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    DatabaseSession getCurrentSession();

    Session getSession();
}
