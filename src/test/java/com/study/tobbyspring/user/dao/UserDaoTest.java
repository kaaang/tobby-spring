package com.study.tobbyspring.user.dao;

import com.study.tobbyspring.user.domain.Level;
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
class UserDaoTest {
    @Autowired private UserDaoJdbc dao;
    @Autowired DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp(){
        this.user1 = new User("1", "name1", "pass1", Level.BASIC, 1, 0);
        this.user2 = new User("2", "name2", "pass2", Level.SILVER, 55, 10);
        this.user3 = new User("3", "name3", "pass3", Level.GOLD, 100, 40);

        dao = new UserDaoJdbc();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/tobby", "root", "root", true
        );
        dao.setDataSource(dataSource);

    }

    @AfterEach
    void tearDown() throws SQLException {
        this.dao.deleteAll();
    }


    @Test
    void addAndGet() throws SQLException {
        this.user1 = new User("1", "name1", "pass1", Level.BASIC, 1, 0);
        this.user2 = new User("2", "name2", "pass2", Level.SILVER, 55, 10);

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);

        User userGet1 = dao.get(user1.getId());
        this.checkSameUser(userGet1, user1);

        User userGet2 = dao.get(user2.getId());
        this.checkSameUser(userGet2, user2);
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

    @Test
    void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("kangShin");
        user1.setPassword("pppppppp");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);

        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }
}