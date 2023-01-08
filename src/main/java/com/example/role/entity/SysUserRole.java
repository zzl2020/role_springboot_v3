package com.example.role.entity;

import lombok.Data;

import java.util.Date;


@Data
public class SysUserRole {

	private Integer id;

	private Integer userId;

	private Integer roleId;

	/**
	* 创建时间
	*/
	private Date createTime;

	private Date updateTime;

}
