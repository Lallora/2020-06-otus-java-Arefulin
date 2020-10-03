package ru.otus.flyway;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationsExecutorFlyway implements MigrationsExecutor {
    private static Logger logger = LoggerFactory.getLogger(MigrationsExecutor.class);

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String hibernateConfigResourceFileName) {
        Configuration configuration = new Configuration().configure(hibernateConfigResourceFileName);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
                .load();
    }

    @Override
    public void cleanDb() {
        flyway.clean();
    }

    @Override
    public void executeMigrations() {
        logger.info("db migration started...");
        flyway.migrate();
        logger.info("db migration finished.");
    }
}
