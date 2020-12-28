package hw17;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import hw17.config.AppConfig;
import hw17.messageservice.MessageSystemClient;

@ComponentScan("hw17")
public class DBServer {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        final var client = ctx.getBean(MessageSystemClient.class);
        client.connect();
    }
}
