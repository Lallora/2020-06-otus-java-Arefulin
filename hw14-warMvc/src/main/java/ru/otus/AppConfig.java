package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.cache.HwCache;
import ru.otus.cache.MyCache;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;

@Configuration
public class AppConfig {

    @Bean
    public <K, V> HwCache<K, V> cache() {
        return new MyCache<>();
    }

    @Bean
    public SessionFactory buildSessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
        MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
        metadataSources.addAnnotatedClass(User.class);
        metadataSources.addAnnotatedClass(Address.class);
        metadataSources.addAnnotatedClass(Phone.class);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    private static StandardServiceRegistry createServiceRegistry(org.hibernate.cfg.Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }
}
