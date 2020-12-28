package hw17.hibernate.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import hw17.core.dao.UserDao;
import hw17.core.dao.UserDaoException;
import hw17.core.model.User;
import hw17.core.sessionmanager.SessionManager;
import hw17.hibernate.sessionmanager.DatabaseSessionHibernate;
import hw17.hibernate.sessionmanager.SessionManagerHibernate;

@Repository
public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<User> findById(long id) {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final EntityGraph<User> graph = (EntityGraph<User>) currentSession.getHibernateSession()
                    .getEntityGraph("graph.userEntity.addresesAndPhones");
            final Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.loadgraph", graph);

            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id, properties));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public Optional<User> findByName(String name) {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final EntityGraph<User> graph = (EntityGraph<User>) currentSession.getHibernateSession()
                    .getEntityGraph("graph.userEntity.addresesAndPhones");

            final Session hibernateSession = currentSession.getHibernateSession();
            final CriteriaBuilder cb = hibernateSession.getCriteriaBuilder();
            final CriteriaQuery<User> cq = cb.createQuery(User.class);
            final Root<User> rootEntry = cq.from(User.class);
            final CriteriaQuery<User> filterByName = cq.select(rootEntry).where(cb.equal(rootEntry.get("name"), name));
            TypedQuery<User> query = hibernateSession.createQuery(filterByName);
            return Optional.of(query.setHint("javax.persistence.loadgraph", graph).getSingleResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final EntityGraph<User> graph = (EntityGraph<User>) currentSession.getHibernateSession()
                    .getEntityGraph("graph.userEntity.addresesAndPhones");

            final Session hibernateSession = currentSession.getHibernateSession();
            final CriteriaBuilder cb = hibernateSession.getCriteriaBuilder();
            final CriteriaQuery<User> cq = cb.createQuery(User.class);
            final Root<User> rootEntry = cq.from(User.class);
            final CriteriaQuery<User> all = cq.select(rootEntry);
            TypedQuery<User> allQuery = hibernateSession.createQuery(all);
            return allQuery.setHint("javax.persistence.loadgraph", graph).setMaxResults(1_000).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertUser(User user) {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        final DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            final Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
