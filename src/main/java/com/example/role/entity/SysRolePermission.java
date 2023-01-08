package com.example.role.entity;

import lombok.Data;

import java.util.Date;


@Data
public class SysRolePermission {

	private Integer id;

	/**
	* 角色id
	*/
	private Integer roleId;

	/**
	* 权限id
	*/
	private Integer permissionId;

	private Date createTime;

	private Date updateTime;

}
