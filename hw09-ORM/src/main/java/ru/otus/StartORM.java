package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.models.User;
import ru.otus.core.service.DbServiceUser;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.DaoUserJDBC;
import ru.otus.jdbc.mapper.IJdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManager;

import javax.sql.DataSource;
import java.util.Optional;


public class StartORM {
    private static final Logger logger = LoggerFactory.getLogger(StartORM.class);

    public static void main(String[] args) throws Exception {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);

        var sessionManager = new SessionManager(dataSource);
        sessionManager.beginSession();
// Работа с пользователем
        DbExecutor<User> dbExecutor = new DbExecutor<>();
        var daoUser = new DaoUserJDBC(sessionManager, dbExecutor);

// Создадим экземпляры класса User
        User userMike = new User();
        userMike.setName("Миша");
        userMike.setAge(27);

        User userAnn = new User();
        userAnn.setName("Анна");
        userAnn.setAge(22);

        User userPeter = new User();
        userPeter.setName("Петя");
        userPeter.setAge(3);

        User userFedor = new User();
        userFedor.setName("Фёдор");
        userFedor.setAge(51);

// Сделаем записи в таблице user с помощью нашей ORM
        IJdbcMapper<User> jdbcMapperUser = new JdbcMapper<>(User.class, sessionManager);
        jdbcMapperUser.insert(userFedor); // 1
        jdbcMapperUser.insert(userMike);  // 2 Mike
        jdbcMapperUser.insert(userMike);  // 3 Mike
        jdbcMapperUser.insert(userMike);  // 4 Mike
        jdbcMapperUser.insert(userMike);  // 5 Mike
        jdbcMapperUser.update(new User(3, "updated_Миша", 30));
        jdbcMapperUser.insertOrUpdate(new User(4, "insertedOrUpdated_Миша_id4", 32));
        jdbcMapperUser.insertOrUpdate(new User(26, "insertedOrUpdated_Миша_id26", 33));

// Код дальше должен остаться, т.е. daoUser должен использоваться
        var dbServiceUser = new DbServiceUser(daoUser);
        var id2 = dbServiceUser.saveUser(userAnn);
        var id3 = dbServiceUser.saveUser(userPeter);
        var id4 = dbServiceUser.saveUser(new User(0, "Маша", 1));
        Optional<User> user0 = dbServiceUser.getUser(1);
        Optional<User> user1 = dbServiceUser.getUser(2);
        Optional<User> user2 = dbServiceUser.getUser(3);
        Optional<User> user3 = dbServiceUser.getUser(4);
        Optional<User> user4 = dbServiceUser.getUser(5);
        Optional<User> user5 = dbServiceUser.getUser(6);
        Optional<User> user6 = dbServiceUser.getUser(id2);
        Optional<User> user7 = dbServiceUser.getUser(id3);
        Optional<User> user8 = dbServiceUser.getUser(id4);

        user1.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
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
