package com.example.role.commons.jwt.entity;
import lombok.Data;

import java.util.Date;
@Data
public class Token {
    private Integer id;
    private String openId;
    private String role;
    private Date lastLogin;
}

