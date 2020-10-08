package ru.otus.flyway;

public interface MigrationsExecutor {
    void cleanDb();
    void executeMigrations();
}
