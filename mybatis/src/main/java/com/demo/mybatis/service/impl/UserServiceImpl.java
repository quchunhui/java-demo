package com.demo.mybatis.service.impl;

import com.demo.mybatis.entity.User;
import com.demo.mybatis.mapper.UserMapper;
import com.demo.mybatis.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public Integer insertOne(User user) {
        return userMapper.insertOne(user);
    }

    @Override
    public Integer updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    @Override
    public Integer deleteUserById(int id) {
        return userMapper.deleteUserById(id);
    }
}
