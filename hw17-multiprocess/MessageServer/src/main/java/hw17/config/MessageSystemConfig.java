package hw17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hw17.messageservice.MessageService;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;

@Configuration
public class MessageSystemConfig {
    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean
    public MessageService messageService(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        return new MessageService(messageSystem, callbackRegistry);
    }
}
