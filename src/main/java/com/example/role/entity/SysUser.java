package com.example.role.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SysUser {

	private Integer id;

	/**
	* 用户名
	*/
	private String userName;

	/**
	* 密码
	*/
	private String password;

	/**
	* 图像存储路径
	*/
	private String icon;

	/**
	* 个人描述
	*/
	private String desc;

	/**
	* 手机号
	*/
	private String phone;

	/**
	* 0-启用,1-禁用
	*/
	private Integer status;

	/**
	* 邮箱
	*/
	private String email;

	private Date createTime;

	private Date updateTime;
	private List<SysRole> roles;
}
