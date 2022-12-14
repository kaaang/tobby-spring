package com.study.tobbyspring.user.dao;

import com.study.tobbyspring.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@SpringBootTest
//@ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(locations = "/test-applicationContext.xml")
//@DirtiesContext
class UserDaoTest {
//    @Autowired ApplicationContext context;
//    @Autowired private UserDao dao;
    private UserDao dao;

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

        dao = new UserDao();
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
}