package com.study.tobbyspring.user.service;

import com.study.tobbyspring.user.dao.UserDao;
import com.study.tobbyspring.user.domain.Level;
import com.study.tobbyspring.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
    UserDao userDao;
    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void upgradeLevels() throws SQLException {
//        TransactionSynchronizationManager.initSynchronization();
//        Connection c = DataSourceUtils.getConnection(dataSource);
//        c.setAutoCommit(false);
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }
//            c.commit();
            this.transactionManager.commit(status);
        } catch (Exception e) {
//            c.rollback();
            this.transactionManager.rollback(status);
            e.printStackTrace();
        } finally {
//            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel){
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw  new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User user){
        if(user.getLevel() == null){
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    static class TestUserService extends UserService{
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getLevel().equals(this.id)){
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{

    }
}
