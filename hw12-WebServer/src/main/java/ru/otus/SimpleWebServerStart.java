package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;

import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoImpl;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.flyway.MigrationsExecutor;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.WebServerWithAuth;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

*/
public class SimpleWebServerStart {
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        UserDao userDao = new UserDaoImpl(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        UsersWebServer usersWebServer = new WebServerWithAuth(templateProcessor, loginService, dbServiceUser, gson);

        usersWebServer.start();
    }
}
