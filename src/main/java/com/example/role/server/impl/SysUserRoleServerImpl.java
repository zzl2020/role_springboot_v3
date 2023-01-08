package com.example.role.server.impl;


import com.example.role.entity.SysUserRole;
import com.example.role.mapper.SysUserRoleMapper;
import com.example.role.server.SysUserRoleServer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SysUserRoleServerImpl implements SysUserRoleServer {
    @Resource
    public SysUserRoleMapper sysUserRoleMapper;

    public int insert(SysUserRole sysUserRole) {
        return sysUserRoleMapper.insert(sysUserRole);
    }

    public SysUserRole findById(Integer id) {
        return sysUserRoleMapper.findById(id);
    }

    public List<SysUserRole> findAll() {
        return sysUserRoleMapper.findAll();
    }

    public int update(SysUserRole sysUserRole) {
        return sysUserRoleMapper.update(sysUserRole);
    }

    public int delete(Integer id) {
        return sysUserRoleMapper.delete(id);
    }
}