package com.example.role.server;


import com.example.role.commons.util.R;
import com.example.role.entity.SysRole;
import com.example.role.entity.dto.RoleDto;

import java.util.List;

public interface SysRoleServer {
	/**
	*添加数据
	**/
	int insert(SysRole sysRole);

	/**
	*根据id查询数据
	**/
	SysRole findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysRole> findAll();

	/**
	*修改数据
	**/
	int update(SysRole sysRole);

	/**
	*删除数据
	**/
	int delete(Integer id);
	/**
	 * 给角色赋权限
	 * */
    R roleAddPermission(RoleDto roleDto);
	/**
	 * 取消角色权限
	 * */
	R cancelPermission( List<Integer> ids);
	R getRolePermission(Integer id);
}
