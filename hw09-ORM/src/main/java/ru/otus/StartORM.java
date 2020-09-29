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
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;
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
        var classMetaDataUser = new EntityClassMetaDataImpl(User.class);
        var sqlMetaDataUser = new EntitySQLMetaDataImpl(classMetaDataUser);
        var jdbcMapperUser = new JdbcMapperImpl(sessionManager, dbExecutorUser, classMetaDataUser, sqlMetaDataUser);
        var userDao = new DaoUserJDBC(sessionManager, jdbcMapperUser);
        var dbServiceUser = new DbServiceUserImpl(userDao);
// Insert
        dbServiceUser.saveUser(new User(0, "Petrova", 24));
        dbServiceUser.saveUser(new User(0, "Ivanov", 37));
        Optional<User> user1 = dbServiceUser.getUser(1);
        Optional<User> user2 = dbServiceUser.getUser(2);
        user1.ifPresentOrElse(crUser -> logger.info("created user1, name:{}", crUser.getName()),
                () -> logger.info("user1 was not created"));
        user2.ifPresentOrElse(crUser -> logger.info("created user2, name:{}", crUser.getName()),
                () -> logger.info("user2 was not created"));
        dbServiceUser.saveUser(new User(0, "Sidorov", 50));
        Optional<User> user3 = dbServiceUser.getUser(3);
        user3.ifPresentOrElse(crUser -> logger.info("created user3, name:{}", crUser.getName()),
                () -> logger.info("user3 was not created"));
// Update or insert
        dbServiceUser.saveUser(new User(125, "Kuznecova", 55));
        Optional<User> user4 = dbServiceUser.getUser(4);
        user4.ifPresentOrElse(crUser -> logger.info("created user4, name:{}", crUser.getName()),
                () -> logger.info("user4 was not created"));
// Update
        dbServiceUser.getUser(1);
        dbServiceUser.saveUser(new User(1, "Ivanova", 25));
        dbServiceUser.getUser(1);
// Accounts
        DbExecutor<Account> dbExecutorAccount = new DbExecutorImpl<>();
        var classAccountMetaDataAccount = new EntityClassMetaDataImpl(Account.class);
        var sqlAccountMetaDataAccount = new EntitySQLMetaDataImpl(classAccountMetaDataAccount);
        var jdbcMapperAccount = new JdbcMapperImpl(sessionManager, dbExecutorAccount, classAccountMetaDataAccount,
                sqlAccountMetaDataAccount);
        var accountDao = new DaoAccountJDBC(sessionManager, jdbcMapperAccount);
        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        // Insert
        dbServiceAccount.saveAccount(new Account(0, "acc001", 1));
        dbServiceAccount.saveAccount(new Account(0, "acc002", 2));
        Optional<Account> account1 = dbServiceAccount.getAccount(1);
        Optional<Account> account2 = dbServiceAccount.getAccount(2);
        account1.ifPresentOrElse(crAccount -> logger.info("created Account1, name:{}", crAccount.getType()),
                () -> logger.info("Account1 was not created"));
        account2.ifPresentOrElse(crAccount -> logger.info("created Account2, name:{}", crAccount.getType()),
                () -> logger.info("Account2 was not created"));
        dbServiceAccount.saveAccount(new Account(0, "acc003", 3));
        Optional<Account> account3 = dbServiceAccount.getAccount(3);
        account3.ifPresentOrElse(crAccount -> logger.info("created Account3, name:{}", crAccount.getType()),
                () -> logger.info("Account3 was not created"));
// Update or insert
        dbServiceAccount.saveAccount(new Account(125, "acc004", 4));
        Optional<Account> account4 = dbServiceAccount.getAccount(4);
        account4.ifPresentOrElse(crAccount -> logger.info("created Account4, name:{}", crAccount.getType()),
                () -> logger.info("Account4 was not created"));
// Update
        dbServiceAccount.getAccount(1);
        dbServiceAccount.saveAccount(new Account(1, "acc005", 5));
        dbServiceAccount.getAccount(1);
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
