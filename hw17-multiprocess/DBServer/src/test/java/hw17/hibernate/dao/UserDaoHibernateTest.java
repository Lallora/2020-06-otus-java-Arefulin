package hw17.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hw17.core.dao.UserDaoException;
import hw17.core.model.AdressDataSet;
import hw17.core.model.PhoneDataSet;
import hw17.core.model.User;
import hw17.hibernate.HibernateUtils;
import hw17.hibernate.sessionmanager.SessionManagerHibernate;

class UserDaoHibernateTest {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";
    private SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate userDaoHibernate;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class,
                AdressDataSet.class, PhoneDataSet.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    private EntityStatistics getEntityStatistic(Class<?> clazz) {
        final Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(clazz.getName());
    }

    @Test
    void shouldCorrectFindUserById() {
        User expectedUser = new User("Name");
        expectedUser.setPassword("11111");
        expectedUser.setAdress(new AdressDataSet("some street"));
        expectedUser.addPhone(new PhoneDataSet("12345"));
        expectedUser.addPhone(new PhoneDataSet("54321"));

        saveUserDao(expectedUser);
        assertThat(expectedUser.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(expectedUser.getId());
        sessionManagerHibernate.commitSession();
        assertThat(mayBeUser.isPresent()).isTrue();
        assertThat(mayBeUser.get()).isEqualTo(expectedUser);
    }

    @Test
    public void getUserByNameWhenDbEmpty() {
        sessionManagerHibernate.beginSession();
        assertThrows(UserDaoException.class, () -> userDaoHibernate.findByName("Name"));
    }

    @Test
    public void getUserByName() {
        User expectedUser = new User("Name");
        expectedUser.setPassword("11111");
        expectedUser.setAdress(new AdressDataSet("some street"));
        expectedUser.addPhone(new PhoneDataSet("12345"));
        expectedUser.addPhone(new PhoneDataSet("54321"));
        saveUserDao(expectedUser);
        assertThat(expectedUser.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findByName("Name");
        sessionManagerHibernate.commitSession();
        assertThat(mayBeUser).isPresent();
        assertThat(mayBeUser.get()).isEqualTo(expectedUser);
    }

    @Test
    public void getAllUsersInEmptyDbTest() {
        sessionManagerHibernate.beginSession();
        final List<User> users = userDaoHibernate.getAllUsers();
        sessionManagerHibernate.commitSession();
        assertThat(users).isEmpty();
    }

    @Test
    public void getAllUsersTest() {
        saveUserDao(new User("Name1"));
        saveUserDao(new User("Name2"));

        sessionManagerHibernate.beginSession();
        final List<User> users = userDaoHibernate.getAllUsers();
        sessionManagerHibernate.commitSession();

        assertThat(users).hasSize(2);
    }

    @Test
    void shouldCorrectSaveNewUser() {
        User newUser = new User("Name");
        newUser.setPassword("11111");
        newUser.setAdress(new AdressDataSet("some street"));
        newUser.addPhone(new PhoneDataSet("12345"));
        newUser.addPhone(new PhoneDataSet("54321"));

        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(newUser);
        long newUserId = newUser.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newUserId).isGreaterThan(0);

        final Optional<User> actualUser = findUserDao(newUserId);
        assertThat(actualUser.isPresent()).isTrue();
        assertThat(actualUser).get().isEqualTo(newUser);

        assertThat(getEntityStatistic(User.class).getUpdateCount()).isZero();
        assertThat(getEntityStatistic(AdressDataSet.class).getUpdateCount()).isZero();
        assertThat(getEntityStatistic(PhoneDataSet.class).getUpdateCount()).isZero();
    }

    @Test
    void shouldCorrectSaveExistUser() {
        final User existedUser = new User("Name");
        existedUser.setPassword("11111");
        existedUser.setAdress(new AdressDataSet("some street"));
        existedUser.addPhone(new PhoneDataSet("12345"));
        existedUser.addPhone(new PhoneDataSet("54321"));

        saveUserDao(existedUser);
        final long existedUserId = existedUser.getId();
        assertThat(existedUserId).isGreaterThan(0);

        final Optional<User> mayBeModifiedUser = findUserDao(existedUserId);
        assertThat(mayBeModifiedUser.isPresent()).isTrue();
        final User modifiedUser = mayBeModifiedUser.get();
        modifiedUser.setName("New Name");
        modifiedUser.getAdress().setStreet("new street");
        modifiedUser.removePhone(modifiedUser.getPhones().get(0));
        modifiedUser.getPhones().get(0).setNumber("11111");
        saveUserDao(modifiedUser);

        final Optional<User> mayBeUpdatedUser = findUserDao(mayBeModifiedUser.get().getId());
        assertThat(mayBeUpdatedUser.isPresent()).isTrue();
        final User updatedUser = mayBeUpdatedUser.get();
        assertThat(existedUser.getId()).isEqualTo(updatedUser.getId());

        assertThat(modifiedUser).isEqualTo(updatedUser);
    }

    private void saveUserDao(User user) {
        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(user);
        sessionManagerHibernate.commitSession();
    }

    private Optional<User> findUserDao(long id) {
        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(id);
        sessionManagerHibernate.commitSession();
        return mayBeUser;
    }

    @Test
    void getSessionManager() {
        assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }
}
