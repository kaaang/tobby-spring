package com.study.tobbyspring.user.dao;

public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(Throwable cause){
        super(cause);
    }
}

