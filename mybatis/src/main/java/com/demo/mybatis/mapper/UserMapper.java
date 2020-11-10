package com.demo.mybatis.mapper;

import com.demo.mybatis.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    List<User> findAll();
    Integer insertOne(User user);
    Integer updateUserById(User user);
    Integer deleteUserById(int id);
}
