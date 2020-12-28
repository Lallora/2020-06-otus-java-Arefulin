package hw17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import hw17.messageclient.MessageClient;
import hw17.messageclient.network.ClientNIO;
import hw17.messageclient.network.NetworkClient;

@Configuration
@EnableWebSocketMessageBroker
public class AppConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/users-service-websocket").withSockJS();
    }

    @Bean
    public MessageClient messageClient() {
        final String HOST = "localhost";
        final int PORT = 8080;
        final NetworkClient client = new ClientNIO(HOST, PORT);
        final String CLIENT_NAME = "front";
        return new MessageClient(CLIENT_NAME, client);
    }
}
