package com.example.role.entity.dto;


import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;



@Data
public class UpdateUserDto {
    @NotNull(message = "账号不能为空")
    private String userName;
    private String password;
    private String desc;
    @NotNull(message = "头像不能为空")
    private String icon;
    @NotNull(message = "电子邮件不能为空")
    @Email(message = "电子邮件格式错误")
    private String email;
    @NotNull(message = "手机号码不能为空")
    private String phone;//不可更改
}
