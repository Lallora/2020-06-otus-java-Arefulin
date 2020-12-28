package hw17.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import hw17.core.dao.UserDao;
import hw17.core.model.User;
import hw17.core.sessionmanager.SessionManager;

public class DbServiceUserImplTest {
    @Mock
    UserDao userDao;

    @Mock
    SessionManager sessionManager;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserTest() {
        Mockito.when(userDao.getSessionManager()).thenReturn(sessionManager);
        final DBServiceUser dbUserService = new DbServiceUserImpl(userDao, null);
        dbUserService.getUser(1L);
        Mockito.verify(userDao, Mockito.times(1)).getSessionManager();
        Mockito.verify(userDao, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getUserByName() {
        Mockito.when(userDao.getSessionManager()).thenReturn(sessionManager);
        final DBServiceUser dbUserService = new DbServiceUserImpl(userDao, null);
        dbUserService.getUserByName("Name");
        Mockito.verify(userDao, Mockito.times(1)).getSessionManager();
        Mockito.verify(userDao, Mockito.times(1)).findByName("Name");
    }

    @Test
    public void saveUserTest() {
        Mockito.when(userDao.getSessionManager()).thenReturn(sessionManager);
        final DBServiceUser dbUserService = new DbServiceUserImpl(userDao, null);
        dbUserService.saveUser(new User("name"));
        Mockito.verify(userDao, Mockito.times(1)).getSessionManager();
        Mockito.verify(userDao, Mockito.times(1)).insertOrUpdate(Mockito.any());
    }

    @Test
    public void getAllUsersTest() {
        Mockito.when(userDao.getSessionManager()).thenReturn(sessionManager);
        final DBServiceUser dbUserService = new DbServiceUserImpl(userDao, null);
        dbUserService.getAllUsers();
        Mockito.verify(userDao, Mockito.times(1)).getSessionManager();
        Mockito.verify(userDao, Mockito.times(1)).getAllUsers();
    }
}
