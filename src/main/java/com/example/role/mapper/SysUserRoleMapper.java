package com.example.role.mapper;


import com.example.role.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysUserRoleMapper {
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
