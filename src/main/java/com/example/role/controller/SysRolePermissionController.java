package com.example.role.controller;


import com.example.role.entity.SysRolePermission;
import com.example.role.server.SysRolePermissionServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/sysrolepermission")
@RestController()
public class SysRolePermissionController {
    @Resource
    private SysRolePermissionServer sysRolePermissionserver;

    @PostMapping("/insert")
    public int insert(@RequestBody SysRolePermission sysRolePermission) {
        return sysRolePermissionserver.insert(sysRolePermission);
    }

    @PostMapping("/update")
    public int update(@RequestBody SysRolePermission sysRolePermission) {
        return sysRolePermissionserver.update(sysRolePermission);
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return sysRolePermissionserver.delete(id);
    }

    @GetMapping("/findAll")
    public List<SysRolePermission> findAll() {
        return sysRolePermissionserver.findAll();
    }

    @GetMapping("/findById/{id}")
    public SysRolePermission findById(@PathVariable Integer id) {
        return sysRolePermissionserver.findById(id);
    }
}