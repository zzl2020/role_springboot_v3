package com.example.role.server;


import com.example.role.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleServer {
	/**
	*添加数据
	**/
	int insert(SysUserRole sysUserRole);

	/**
	*根据id查询数据
	**/
	SysUserRole findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysUserRole> findAll();

	/**
	*修改数据
	**/
	int update(SysUserRole sysUserRole);

	/**
	*删除数据
	**/
	int delete(Integer id);

}
