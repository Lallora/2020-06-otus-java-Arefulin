package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.flyway.MigrationsExecutor;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StartHibernate {

    private static final Logger logger = LoggerFactory.getLogger(StartHibernate.class);
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

        long id = dbServiceUser.saveUser(user);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

        outputUserOptional("Created user", mayBeCreatedUser);

        user.setName("Sergey Semenovich");

        dbServiceUser.saveUser(user);

        Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(id);
        outputUserOptional("Updated user", mayBeUpdatedUser);


        List<Phone> phones2 = new ArrayList<>();
        Phone pds2 = new Phone("+7 (495) 633-51-90");
        Phone pds3 = new Phone("+7 (495) 957-93-63");
        phones2.add(pds2);
        phones2.add(pds3);

        Address address1 = new Address("Voznesenskiy per., 21, Moscow, 125032");
        User user1 = new User("Michael", 54, address1, phones2);

        long id1 = dbServiceUser.saveUser(user1);

        Optional<User> mayBeCreatedUser1 = dbServiceUser.getUser(id1);

        outputUserOptional("Created user", mayBeCreatedUser1);
    }

    private static void outputUserOptional(String header, Optional<User> mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
    }
}
