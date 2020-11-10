package com.demo.mybatis.controller;

import com.demo.mybatis.entity.User;
import com.demo.mybatis.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findAll")
    public List<User> findAll(){
        return userService.findAll();
    }

    @ResponseBody
    @PostMapping("/insertOne")
    public Integer insertOne(@RequestBody User user) {
        return userService.insertOne(user);
    }

    @ResponseBody
    @PostMapping("/updateUserById")
    public Integer updateUserById(@RequestBody User user) {
        return userService.updateUserById(user);
    }

    @ResponseBody
    @PostMapping("/deleteUserById")
    public Integer deleteUserById(@RequestBody int id) {
        return userService.deleteUserById(id);
    }
}
