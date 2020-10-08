package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HWCacheDemo;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.service.CachedDBServiceUser;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.flyway.MigrationsExecutor;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;



public class StartCache {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
// Общая часть
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        // create user
        List<Phone> phones = new ArrayList<>();
        Phone pds = new Phone("+7 (495) 620-27-00");
        phones.add(pds);
        Address address = new Address("Tverskaya st., 13, Moscow, 125009");
        User user = new User("Sergey", 62, address, phones);




        HwCache<String, User> cache = new MyCache<>();
        DBServiceUser cachedDBServiceUser = new CachedDBServiceUser(dbServiceUser, cache);
        HwListener<String, User> listener = (key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action);

        cache.addListener(listener);

        long id = cachedDBServiceUser.saveUser(user);

        long startTime = System.currentTimeMillis();
        dbServiceUser.getUser(1);
        for (int i = 0; i < 100; i++) {
            cachedDBServiceUser.getUser(id).get();
        }
        long endTime = System.currentTimeMillis();

        long startTime2 = System.currentTimeMillis();
        dbServiceUser.getUser(1);
        for (int i = 0; i < 100; i++) {
            dbServiceUser.getUser(id);
        }
        long endTime2 = System.currentTimeMillis();

        System.out.println("Total execution time with cache: " + (endTime-startTime) + "ms");
        System.out.println("Total execution time without cache: " + (endTime2-startTime2) + "ms");

    }
}
