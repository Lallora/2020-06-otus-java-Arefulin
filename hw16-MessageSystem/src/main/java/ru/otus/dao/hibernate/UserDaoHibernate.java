package ru.otus.dao.hibernate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.sessionmanager.hibernate.SessionManagerHibernate;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoHibernate implements UserDao {
    @Getter
    private final SessionManagerHibernate sessionManager;

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(getSession().find(User.class, id));
    }

    @Override
    public long insertUser(User user) {
        getSession().persist(user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        getSession().merge(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        if (user.getId() > 0) {
            getSession().merge(user);
        } else {
            getSession().persist(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        return getSession().createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> findAny() {
        return Optional.ofNullable(getSession().createQuery("SELECT u FROM User u ORDER BY rand()", User.class)
                .setMaxResults(1)
                .getSingleResult());
    }

    private EntityManager getSession() {
        return sessionManager.getCurrentSession().getSession();
    }
}
