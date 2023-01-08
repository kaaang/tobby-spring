package com.study.tobbyspring.user.service;

import com.study.tobbyspring.user.dao.UserDaoJdbc;
import com.study.tobbyspring.user.domain.Level;
import com.study.tobbyspring.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.study.tobbyspring.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.study.tobbyspring.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserServiceTest {
    @Autowired private UserDaoJdbc userDao;
    @Autowired private UserService userService;
    @Autowired DataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp(){
        userDao = new UserDaoJdbc();
        dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/tobby", "root", "root", true
        );
        userDao.setDataSource(dataSource);

        userService = new UserService();
        userService.setUserDao(userDao);

        users = Arrays.asList(
                new User("qjswls","범진","p1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1 ,0),
                new User("audtjd","명성","p2", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER ,0),
                new User("tmdghks","승한","p3", Level.SILVER,MIN_RECCOMEND_FOR_GOLD-1 ,29),
                new User("tkdgh","상호","p4", Level.SILVER,MIN_RECCOMEND_FOR_GOLD ,30),
                new User("alsrb","민규","p5", Level.GOLD,100 ,Integer.MAX_VALUE)
        );
    }

    @Test
    void upgradeLevels() throws SQLException {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0),false);
        checkLevel(users.get(1),true);
        checkLevel(users.get(2),false);
        checkLevel(users.get(3),true);
        checkLevel(users.get(4),false);
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeAllOrNothing() {
        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setDataSource(this.dataSource);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try{
            testUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
        }catch (UserService.TestUserServiceException | SQLException e){

        }


    }

    void checkLevel(User user, Boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }else{
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }
}