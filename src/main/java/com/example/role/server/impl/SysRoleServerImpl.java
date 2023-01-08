package com.example.role.server.impl;


import com.example.role.commons.util.R;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.entity.SysRole;
import com.example.role.entity.SysRolePermission;
import com.example.role.entity.dto.RoleDto;
import com.example.role.mapper.SysRoleMapper;
import com.example.role.mapper.SysRolePermissionMapper;
import com.example.role.server.CommonServer;
import com.example.role.server.SysPermissionServer;
import com.example.role.server.SysRoleServer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.List;

@Service
public class SysRoleServerImpl implements SysRoleServer {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Resource
    private CommonServer commonServer;
    @Resource
    private SysPermissionServer permissionServer;

    public int insert(SysRole sysRole) {
        return sysRoleMapper.insert(sysRole);
    }

    public SysRole findById(Integer id) {
        return sysRoleMapper.findById(id);
    }

    public List<SysRole> findAll() {
        return sysRoleMapper.findAll();
    }

    public int update(SysRole sysRole) {
        return sysRoleMapper.update(sysRole);
    }

    public int delete(Integer id) {
        return sysRoleMapper.delete(id);
    }

    @Override
    public R roleAddPermission(RoleDto roleDto) {
        Integer roleId = roleDto.getRoleId();
        Assert.notNull(roleId,"roleId is null");
        List<Integer> pids = roleDto.getPermissionIds();
        Assert.notNull(roleDto, "roleDto is null");
        if (pids.size() == 0) {
            throw new CustomException();
        }

        SysRolePermission sp = new SysRolePermission();
        List<SysRolePermission> all = sysRolePermissionMapper.findAll();
        sp.setRoleId(roleId);
        pids.forEach(t -> {

            Assert.notNull(t," pid is null");
            for (SysRolePermission sysRolePermission : all) {
                Integer roleId1 = sysRolePermission.getRoleId();
                Integer permissionId = sysRolePermission.getPermissionId();
                if(roleId1==roleId&&t==permissionId){
                    throw new CustomException(500,"您已拥有"+permissionServer.findById(permissionId).getName()+"权限");
                }
            }
            sp.setPermissionId(t);
            sysRolePermissionMapper.insert(sp);
        });
        return R.ok();
    }

    @Override
    public R cancelPermission( List<Integer> ids) {
        Assert.notNull(ids, "ids is null");
        ids.forEach(id -> {
            Assert.notNull(id," id is null");
            sysRolePermissionMapper.delete(id);
        });
        return R.ok();
    }

    @Override
    public R getRolePermission(Integer id) {
        return R.ok(sysRoleMapper.getRolePermission(id));
    }

}