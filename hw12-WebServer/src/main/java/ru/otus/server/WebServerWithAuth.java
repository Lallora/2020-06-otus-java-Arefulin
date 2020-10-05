package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.core.service.DBServiceUser;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.UsersServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebServerWithAuth implements UsersWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String CONSTRAINT_NAME = "auth";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_GUEST = "guest";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final int WEB_SERVER_PORT = 8080;

    private final TemplateProcessor templateProcessor;
    private final Server server;
    private final LoginService loginService;
    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public WebServerWithAuth(TemplateProcessor templateProcessor,
                             LoginService loginService,
                             DBServiceUser dbServiceUser,
                             Gson gson) {
        this.templateProcessor = templateProcessor;
        this.server = new Server(WEB_SERVER_PORT);
        this.loginService = loginService;
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    private HandlerList createResourceHandler() {
        HandlerList handlers = new HandlerList();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));

        handlers.addHandler(resourceHandler);
        handlers.addHandler(this.securityHandler("/users"));

        return handlers;
    }

    private Handler securityHandler(String ...paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_ADMIN, ROLE_NAME_USER, ROLE_NAME_GUEST});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(this.createServletContextHandler()));

        return security;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, dbServiceUser, gson)), "/users");
        return servletContextHandler;
    }

    @Override
    public void start() throws Exception {
        HandlerList handlers = this.createResourceHandler();
        server.setHandler(handlers);
        server.start();

    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }
}
