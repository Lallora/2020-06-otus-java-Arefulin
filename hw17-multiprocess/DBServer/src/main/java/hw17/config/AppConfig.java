package hw17.config;

import hw17.cachehw.HwCache;
import hw17.cachehw.MyCache;
import hw17.core.model.AdressDataSet;
import hw17.core.model.PhoneDataSet;
import hw17.core.model.User;
import hw17.core.service.DBServiceUser;
import hw17.hibernate.HibernateUtils;
import hw17.messageclient.MessageClient;
import hw17.messageclient.network.ClientNIO;
import hw17.messageclient.network.NetworkClient;
import hw17.messageservice.MessageSystemClient;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("hw17")
@Configuration
public class AppConfig {
    private static final String HIBERNATE_CONF = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CONF, User.class, AdressDataSet.class, PhoneDataSet.class);
    }

    @Bean
    public HwCache<String, User> hwCache() {
        return new MyCache<String, User>();
    }

    @Bean
    public MessageClient messageClient() {
        final String HOST = "localhost";
        final int PORT = 8085;
        final NetworkClient client = new ClientNIO(HOST, PORT);
        final String CLIENT_NAME = "db";
        return new MessageClient(CLIENT_NAME, client);
    }

    @Bean
    public MessageSystemClient messageSystemClient(MessageClient messageClient, DBServiceUser dbServiceUser) {
        return new MessageSystemClient(messageClient, dbServiceUser);
    }
}
