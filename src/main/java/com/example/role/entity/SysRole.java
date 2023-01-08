package com.example.role.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SysRole {

	private Integer id;

	/**
	* 角色名称
	*/
	private String roleName;

	/**
	* 角色描述
	*/
	private String roleDesc;

	/**
	* 角色状态0-启用,1-禁用
	*/
	private Integer status;

	private Date createTime;

	private Date updateTime;
	private List<SysPermission> permissions;
}
