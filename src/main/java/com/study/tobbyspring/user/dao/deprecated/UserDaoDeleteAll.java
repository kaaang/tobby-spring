package com.study.tobbyspring.user.dao.deprecated;

import com.study.tobbyspring.user.dao.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public class UserDaoDeleteAll extends UserDao {

    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }

}
