package hw17;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hw17.config.MessageSystemConfig;
import hw17.services.ServerNIO;

public class MessageServer {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ServerNIO.class,
                MessageSystemConfig.class);
    }
}
