package com.example.role.server.impl;


import com.example.role.entity.SysRolePermission;
import com.example.role.mapper.SysRolePermissionMapper;
import com.example.role.server.SysRolePermissionServer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRolePermissionServerImpl implements SysRolePermissionServer {
    @Resource
    public SysRolePermissionMapper sysRolePermissionMapper;

    public int insert(SysRolePermission sysRolePermission) {
        return sysRolePermissionMapper.insert(sysRolePermission);
    }

    public SysRolePermission findById(Integer id) {
        return sysRolePermissionMapper.findById(id);
    }

    public List<SysRolePermission> findAll() {
        return sysRolePermissionMapper.findAll();
    }

    public int update(SysRolePermission sysRolePermission) {
        return sysRolePermissionMapper.update(sysRolePermission);
    }

    public int delete(Integer id) {
        return sysRolePermissionMapper.delete(id);
    }
}