package com.example.role.entity;

import lombok.Data;

import java.util.Date;


@Data
public class SysPermission {

	private Integer id;

	/**
	* 权限路径
	*/
	private String url;

	/**
	* 权限名称
	*/
	private String name;

	/**
	* 权限菜单
	*/
	private Integer pid;

	/**
	* 权限排序
	*/
	private Integer orderNum;

	/**
	* 权限状态0-启用1-禁用
	*/
	private Integer status;

	/**
	* 创建时间
	*/
	private Date createTime;

	/**
	* 更新时间
	*/
	private Date updateTime;

}
