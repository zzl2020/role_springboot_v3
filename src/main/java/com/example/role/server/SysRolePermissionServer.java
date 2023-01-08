package com.example.role.server;


import com.example.role.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionServer {
	/**
	*添加数据
	**/
	int insert(SysRolePermission sysRolePermission);

	/**
	*根据id查询数据
	**/
	SysRolePermission findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysRolePermission> findAll();

	/**
	*修改数据
	**/
	int update(SysRolePermission sysRolePermission);

	/**
	*删除数据
	**/
	int delete(Integer id);

}
