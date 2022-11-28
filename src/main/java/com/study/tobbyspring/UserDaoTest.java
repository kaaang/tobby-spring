package com.study.tobbyspring;

import com.study.tobbyspring.user.dao.DaoFactory;
import com.study.tobbyspring.user.dao.UserDao;

public class UserDaoTest {
    public static void main(String[] args) {
        UserDao dao = new DaoFactory().userDao();
    }
}
