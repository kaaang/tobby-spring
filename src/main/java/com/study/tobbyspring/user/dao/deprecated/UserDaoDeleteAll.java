package com.study.tobbyspring.user.dao.deprecated;

import com.study.tobbyspring.user.dao.UserDaoJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public class UserDaoDeleteAll extends UserDaoJdbc {

    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }

}
