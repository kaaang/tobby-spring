package com.study.tobbyspring;

import com.study.tobbyspring.user.dao.UserDao;
import com.study.tobbyspring.user.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class TobbySpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        SpringApplication.run(TobbySpringApplication.class, args);

//        UserDao dao = new UserDao();
//
//        User user = new User();
//        user.setId("rai");
//        user.setName("kang");
//        user.setPassword("pass");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + " : create");
//        System.out.println();
//
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getPassword() + " : read");
    }

}
