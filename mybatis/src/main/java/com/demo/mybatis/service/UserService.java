package com.demo.mybatis.service;

import com.demo.mybatis.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    Integer insertOne(User user);
    Integer updateUserById(User user);
    Integer deleteUserById(int id);
}