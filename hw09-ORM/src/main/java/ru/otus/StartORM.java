package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.core.models.User;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.DaoAccountJDBC;
import ru.otus.jdbc.dao.DaoUserJDBC;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.jdbc.sessionmanager.SessionManagerImpl;

import javax.sql.DataSource;
import java.util.Optional;

public class StartORM {

    private static final Logger logger = LoggerFactory.getLogger(StartORM.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        SessionManager sessionManager = new SessionManagerImpl(dataSource);
// Users
        DbExecutor<User> dbExecutorUser = new DbExecutorImpl<>();
        var jdbcMapperUser = new JdbcMapperImpl(sessionManager, dbExecutorUser);
        var userDao = new DaoUserJDBC(sessionManager, jdbcMapperUser);
        var dbServiceUser = new DbServiceUserImpl(userDao);
// Insert
        var idUser1 = dbServiceUser.saveUser(new User(0, "Petrova", 24));
        var idUser2 = dbServiceUser.saveUser(new User(0, "Ivanov", 37));
        Optional<User> user1 = dbServiceUser.getUser(idUser1);
        Optional<User> user2 = dbServiceUser.getUser(idUser2);
        user1.ifPresentOrElse(crUser -> logger.info("created user1, name:{}", crUser.getName()),
                () -> logger.info("user1 was not created"));
        user2.ifPresentOrElse(crUser -> logger.info("created user2, name:{}", crUser.getName()),
                () -> logger.info("user2 was not created"));
        var idUser3 = dbServiceUser.saveUser(new User(0, "Sidorov", 50));
        Optional<User> user3 = dbServiceUser.getUser(idUser3);
        user3.ifPresentOrElse(crUser -> logger.info("created user3, name:{}", crUser.getName()),
                () -> logger.info("user3 was not created"));
// Update or insert
        var idUser4 = dbServiceUser.saveUser(new User(125, "Kuznecova", 55));
        Optional<User> user4 = dbServiceUser.getUser(idUser4);
        user4.ifPresentOrElse(crUser -> logger.info("created user4, name:{}", crUser.getName()),
                () -> logger.info("user4 was not created"));
// Update
        dbServiceUser.getUser(idUser1);
        var idUser5 = dbServiceUser.saveUser(new User(1, "Ivanova", 25));
        dbServiceUser.getUser(idUser5);
// Accounts
        DbExecutor<Account> dbExecutorAccount = new DbExecutorImpl<>();
        var jdbcMapperAccount = new JdbcMapperImpl(sessionManager, dbExecutorAccount);
        var accountDao = new DaoAccountJDBC(sessionManager, jdbcMapperAccount);
        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        // Insert
        var idAccount1 = dbServiceAccount.saveAccount(new Account(0, "acc001", 1));
        var idAccount2 = dbServiceAccount.saveAccount(new Account(0, "acc002", 2));
        Optional<Account> account1 = dbServiceAccount.getAccount(idAccount1);
        Optional<Account> account2 = dbServiceAccount.getAccount(idAccount2);
        account1.ifPresentOrElse(crAccount -> logger.info("created Account1, name:{}", crAccount.getType()),
                () -> logger.info("Account1 was not created"));
        account2.ifPresentOrElse(crAccount -> logger.info("created Account2, name:{}", crAccount.getType()),
                () -> logger.info("Account2 was not created"));
        var idAccount3 = dbServiceAccount.saveAccount(new Account(0, "acc003", 3));
        Optional<Account> account3 = dbServiceAccount.getAccount(idAccount3);
            account3.ifPresentOrElse(crAccount -> logger.info("created Account3, name:{}", crAccount.getType()),
                () -> logger.info("Account3 was not created"));
// Update or insert
        var idAccount4 = dbServiceAccount.saveAccount(new Account(125, "acc004", 4));
        Optional<Account> account4 = dbServiceAccount.getAccount(idAccount4);
        account4.ifPresentOrElse(crAccount -> logger.info("created Account4, name:{}", crAccount.getType()),
                () -> logger.info("Account4 was not created"));
// Update
        dbServiceAccount.getAccount(idAccount1);
        var idAccount5 = dbServiceAccount.saveAccount(new Account(1, "acc005", 5));
        dbServiceAccount.getAccount(idAccount5);
}

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
