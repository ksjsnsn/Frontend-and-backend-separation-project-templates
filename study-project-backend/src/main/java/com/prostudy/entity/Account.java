package com.prostudy.entity;

import lombok.Data;

/**
 * 用户对象
 */
@Data
public class Account {
    int id;
    String email;
    String username;
    String password;

}
