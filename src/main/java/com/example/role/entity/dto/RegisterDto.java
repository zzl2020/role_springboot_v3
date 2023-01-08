package com.example.role.entity.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data

public class RegisterDto {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String userName;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 图像存储路径
     */
    @NotNull(message = "头像不能为空")
    private String icon;

    /**
     * 个人描述
     */
    private String desc;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")

    private String phone;
    /**
     * 邮箱
     */

    @NotNull(message = "电子邮件不能为空")
    @Email(message = "电子邮箱格式不正确")
    private String email;

}
