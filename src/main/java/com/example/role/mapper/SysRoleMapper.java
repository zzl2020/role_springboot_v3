package com.example.role.mapper;


import com.example.role.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysRoleMapper {
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
	SysRole getRolePermission(Integer id);
}
