package com.example.role.mapper;


import com.example.role.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysUserMapper {
	/**
	*添加数据
	**/
	int insert(SysUser sysUser);

	/**
	*根据id查询数据
	**/
	SysUser findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysUser> findAll();

	/**
	*修改数据
	**/
	int update(SysUser sysUser);

	/**
	*删除数据
	**/
	int delete(Integer id);
	SysUser getUser(String userName,String phone);
	SysUser getUserAndRoleAndPer(String userName);
	SysUser getUserAndRole(String userName);
	SysUser getUserByPhone(String phone);
}
