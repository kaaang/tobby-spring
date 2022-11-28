package com.study.tobbyspring;

import com.study.tobbyspring.user.dao.ConnectionMaker;
import com.study.tobbyspring.user.dao.DConnectionMaker;
import com.study.tobbyspring.user.dao.UserDao;

public class UserDaoTest {
    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new DConnectionMaker();

        UserDao dao = new UserDao(connectionMaker);
    }
}
