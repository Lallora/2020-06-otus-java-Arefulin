package ru.otus.sessionmanager.hibernate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.sessionmanager.SessionManager;
import ru.otus.sessionmanager.SessionManagerException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class SessionManagerHibernate implements SessionManager {
    @Getter
    @PersistenceContext
    private final EntityManagerFactory sessionFactory;
    private DatabaseSessionHibernate session;

    @Override
    public void beginSession() {
        if (session != null) {
            throw new SessionManagerException("DatabaseSession already begun");
        }
        session = new DatabaseSessionHibernate(sessionFactory.createEntityManager());
    }

    @Override
    public void commitSession() {
        checkSession();
        try {
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkSession();
        try {
            session.getTransaction().rollback();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        if (session != null) {
            try {
                session.close();
                session = null;
            } catch (Exception e) {
                throw new SessionManagerException(e);
            }
        }
    }

    @Override
    public DatabaseSessionHibernate getCurrentSession() {
        checkSession();
        return session;
    }

    private void checkSession() {
        if (session == null) {
            throw new SessionManagerException("DatabaseSession not begun");
        }
    }
}
