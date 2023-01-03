package com.study.tobbyspring.user.service;

import com.study.tobbyspring.user.dao.UserDao;
import com.study.tobbyspring.user.domain.Level;
import com.study.tobbyspring.user.domain.User;

import java.util.List;

public class UserService {
    UserDao userDao;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
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
