package com.study.tobbyspring.user.dao;

import com.study.tobbyspring.user.domain.Level;
import com.study.tobbyspring.user.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * UserDao - 1장은 DB connection 관심사를 어떻게 분리할 것인가에 대해 다루면서 조금씩 코드가 개선 중이다.
 */
public class UserDaoJdbc implements UserDao{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void add(final User user){
        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) value(?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userMapper);
    }



    public void deleteAll(){
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount(){
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });
    }

    public List<User> getAll(){
        return this.jdbcTemplate.query("select * from users order by id", userMapper);
    }

    public void update(User user){
        this.jdbcTemplate.update("update users set name = ?, password =?, level = ?, login = ?, recommend = ? where id = ?",
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

}
