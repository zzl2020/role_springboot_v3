package com.example.role.entity.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class LoginDto {
    private String phone;
    private String userName;
    @NotNull(message = "密码不能为空")
    private String password;
    private String vcode;
}
