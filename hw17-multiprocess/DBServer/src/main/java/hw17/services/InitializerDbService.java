package hw17.services;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import hw17.core.model.User;
import hw17.core.service.DBServiceUser;

@Service
public class InitializerDbService implements InitializerService {
    private final DBServiceUser dbServiceUser;

    public InitializerDbService(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    private void prepareUsers() {
        final var admin = new User("admin");
        admin.setPassword("11111");
        dbServiceUser.saveUser(admin);
        final var user1 = new User("user1");
        user1.setPassword("22222");
        dbServiceUser.saveUser(user1);
    }

    @PostConstruct
    public void onApplicationEvent() {
        prepareUsers();
    }

    @Override
    public void init() {
        prepareUsers();
    }
}
