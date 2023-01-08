package com.example.role.mapper;


import com.example.role.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysPermissionMapper {
	/**
	*添加数据
	**/
	int insert(SysPermission sysPermission);

	/**
	*根据id查询数据
	**/
	SysPermission findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysPermission> findAll();

	/**
	*修改数据
	**/
	int update(SysPermission sysPermission);

	/**
	*删除数据
	**/
	int delete(Integer id);

}
