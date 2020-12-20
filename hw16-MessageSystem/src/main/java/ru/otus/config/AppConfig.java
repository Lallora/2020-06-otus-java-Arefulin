package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.handlers.*;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.service.DBServiceUser;
import ru.otus.service.DefaultUserPopulatorService;
import ru.otus.service.UserPopulatorService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;

@Configuration
public class AppConfig {
    public static final String FRONTEND_CLIENTNAME = "frontend";
    public static final String DB_CLIENTNAME = "db";

    @Bean
    EntityManagerFactory entityManager() {
        return Persistence.createEntityManagerFactory("otus-hibernate");
    }

    @Bean(destroyMethod = "dispose")
    MessageSystem messageSystem() {
        return new MessageSystemImpl(true);
    }

    @Bean
    CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean
    MsClient frontendClient(MessageSystem messageSystem, HandlersStore handlersStore,
            CallbackRegistry callbackRegistry) {
        var client = new MsClientImpl(FRONTEND_CLIENTNAME, messageSystem, handlersStore, callbackRegistry);
        var responseHandler = new UsersResponseHandler(callbackRegistry);
        Arrays.stream(UserMessageType.values()).forEach(type -> handlersStore.addHandler(type, responseHandler));
        messageSystem.addClient(client);
        return client;
    }

    @Bean
    MsClient dbClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry,
            DBServiceUser dbServiceUser) {
        var client = new MsClientImpl(DB_CLIENTNAME, messageSystem, handlersStore, callbackRegistry);
        handlersStore.addHandler(UserMessageType.GET_ALL, new GetUsersRequestHandler(dbServiceUser));
        handlersStore.addHandler(UserMessageType.GET, new GetUserRequestHandler(dbServiceUser));
        handlersStore.addHandler(UserMessageType.ADD, new AddUserRequestHandler(dbServiceUser));
        messageSystem.addClient(client);
        return client;
    }

    @Bean
    @Scope("prototype")
    HandlersStore handlersStore() {
        return new HandlersStoreImpl();
    }

    @Bean(initMethod = "init")
    public UserPopulatorService populatorService(DBServiceUser serviceUser) {
        return new DefaultUserPopulatorService(serviceUser);
    }
}
