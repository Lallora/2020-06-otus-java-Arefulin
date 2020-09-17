package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.impl.AccountDaoImpl;
import ru.otus.core.dao.impl.UserDaoImpl;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
// Общая часть
        var dataSource = new DataSourceH2();
        var sessionManager = new SessionManagerJdbc(dataSource);

        var db = new Main();
// Создаём таблицы-источники данных
        db.createUserTable(dataSource);
        db.createAccountTable(dataSource);

        UserDao userDao = new UserDaoImpl(sessionManager);
        AccountDao accountDao = new AccountDaoImpl(sessionManager);

//  USERS
        User user1 = new User(1, "Alex", 35);
        userDao.insertUser(user1);

        // используем метод с аннотацией
        Optional<User> findUser = userDao.findById(user1.getId());
        logger.info(findUser.toString());

        User user2 = new User(2, "Samara", 20);
        userDao.insertOrUpdate(user2);
        logger.info(userDao.findById(user2.getId()).toString());

//  используем различные методы работы с данными БД
        user2.setAge(25);
        user2.setName("Honey!");
        userDao.insertOrUpdate(user2);
        logger.info(userDao.findById(user2.getId()).toString());

        user2.setName("Half!");
        user2.setAge(35);
        userDao.update(user2);

        logger.info(userDao.findById(user2.getId()).toString());

//  ACCOUNTS
        Account account = new Account(1, "VIP", BigDecimal.valueOf(9999));
        accountDao.insertAccount(account);

        Optional<Account> findAccount = accountDao.findById(account.getNo());
        logger.info(findAccount.toString());

        Account newAccount = new Account(2, "Subscriber", BigDecimal.valueOf(5555));
        accountDao.insertOrUpdate(newAccount);
        logger.info(accountDao.findById(newAccount.getNo()).toString());

        newAccount.setType("Following");
        newAccount.setRest(BigDecimal.valueOf(5595));
        accountDao.insertOrUpdate(newAccount);
        logger.info(accountDao.findById(newAccount.getNo()).toString());

        newAccount.setType("Disciples");
        newAccount.setRest(BigDecimal.valueOf(5955));
        accountDao.update(newAccount);

        logger.info(accountDao.findById(newAccount.getNo()).toString());
    }

    private void createUserTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        }
        System.out.println("Table of users has been created");
    }

    private void createAccountTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
        System.out.println("Table of accounts has been created");
    }
}
