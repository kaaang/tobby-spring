package com.study.tobbyspring.user.dao.deprecated;

@Deprecated
public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(Throwable cause){
        super(cause);
    }
}

