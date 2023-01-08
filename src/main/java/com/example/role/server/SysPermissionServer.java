package com.example.role.server;


import com.example.role.commons.util.R;
import com.example.role.entity.SysPermission;

public interface SysPermissionServer {
	/**
	*添加数据
	**/
	int insert(SysPermission sysPermission);

	/**
	*根据id查询数据
	**/
	SysPermission findById(Integer id);

	/**
     * 查询所有数据
     **/
	R findAll();

	/**
	*修改数据
	**/
	int update(SysPermission sysPermission);

	/**
	*删除数据
	**/
	int delete(Integer id);

}
