package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.Account;
import ru.otus.core.models.User;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.DaoAccountJDBC;
import ru.otus.jdbc.dao.DaoUserJDBC;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerImpl;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;


public class StartORM {
    private static final Logger logger = LoggerFactory.getLogger(StartORM.class);
    private static BigDecimal bigDecimal;

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        SessionManagerImpl sessionManagerImpl = new SessionManagerImpl(dataSource);

// User
        DbExecutorImpl<User> dbExecutorImpl = new DbExecutorImpl<>();
        var jdbcMapperUser = JdbcMapperImpl.forType(User.class, sessionManagerImpl, dbExecutorImpl);
        var userDao = new DaoUserJDBC(sessionManagerImpl, jdbcMapperUser);
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var idUser = dbServiceUser.saveUser(new User(0, "dbServiceUser", 35));
        Optional<User> user = dbServiceUser.getUser(idUser);
        user.ifPresentOrElse(crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Account
        DbExecutorImpl<Account> dbExecutorImpl2 = new DbExecutorImpl<>();
    var jdbcMapperAccount = JdbcMapperImpl.forType(Account.class, sessionManagerImpl, dbExecutorImpl2);
    var accountDao = new DaoAccountJDBC(sessionManagerImpl, jdbcMapperAccount);
    var dbServiceAccount = new DbServiceAccountImpl(accountDao);
    var idAccount = dbServiceAccount.saveAccount(new Account(0, "AccountType1", 10));
    Optional<Account> account = dbServiceAccount.getAccount(idAccount);
        account.ifPresentOrElse(new Consumer<Account>() {
                                    @Override
                                    public void accept(Account crAccount) {
                                        logger.info("created account, type:{}", crAccount.getType());
                                    }
                                },
            () -> logger.info("account was not created")
            );
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
