package com.study.tobbyspring;

import com.study.tobbyspring.user.dao.UserDao;
import com.study.tobbyspring.user.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class TobbySpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SpringApplication.run(TobbySpringApplication.class, args);
    }

}
