package com.study.tobbyspring.user.dao;

import com.study.tobbyspring.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@SpringBootTest
//@ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(locations = "/test-applicationContext.xml")
//@DirtiesContext
class UserDaoTest {
//    @Autowired ApplicationContext context;
    @Autowired private UserDaoJdbc dao;
//    private UserDaoJdbc dao;
    @Autowired DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp(){
        this.user1 = new User("1", "name1", "pass1");
        this.user2 = new User("2", "name2", "pass2");
        this.user3 = new User("3", "name3", "pass3");

//        this.dao = context.getBean("userDao", UserDao.class);

//        DataSource dataSource = new SingleConnectionDataSource(
//          "jdbc:mysql://localhost/testdb", "tobby","tobby", true
//        );
//        dao.setDataSource(dataSource);

        dao = new UserDaoJdbc();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/tobby", "tobby", "tobby", true
        );
        dao.setDataSource(dataSource);

    }

    @AfterEach
    void tearDown() throws SQLException {
        this.dao.deleteAll();
    }


    @Test
    void addAndGet() throws SQLException {
        User user1 = new User("1", "name1", "pass1");
        User user2 = new User("2", "name2", "pass2");

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);

        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName()).isEqualTo(user1.getName());
        assertThat(userGet1.getPassword()).isEqualTo(user1.getPassword());

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName()).isEqualTo(user2.getName());
        assertThat(userGet2.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    @Test
    void getUserFailure() throws SQLException {
        dao.deleteAll();

        assertThat(dao.getCount()).isEqualTo(0);

        assertThatThrownBy(() -> dao.get("unknown_id")).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users1.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    @Test
    void duplicateKey() {
        dao.deleteAll();

        assertThatThrownBy(() -> {
            dao.add(user1);
            dao.add(user1);
        }).isInstanceOf(DataAccessException.class);
    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}