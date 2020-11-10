package com.demo.mybatis.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
}
