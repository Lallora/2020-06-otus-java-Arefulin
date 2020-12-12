package ru.otus.sessionmanager.hibernate;

import lombok.Getter;
import ru.otus.sessionmanager.DatabaseSession;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Getter
public class DatabaseSessionHibernate implements DatabaseSession {
    private final EntityManager session;
    private final EntityTransaction transaction;

    public DatabaseSessionHibernate(EntityManager manager) {
        this.session = manager;
        this.transaction = manager.getTransaction();
        transaction.begin();
    }

    public void close() {
        if (transaction.isActive()) {
            transaction.commit();
        }
        session.close();
    }
}
