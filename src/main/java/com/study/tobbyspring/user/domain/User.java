package com.study.tobbyspring.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
public class User {
//    @Id
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;

    public void upgradeLevel(){
        Level nextLevel = this.level.nextLevel();
        if(nextLevel == null){
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }else{
            this.level = nextLevel;
        }
    }
}
