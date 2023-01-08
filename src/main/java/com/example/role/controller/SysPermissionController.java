package com.example.role.controller;


import com.example.role.commons.util.R;
import com.example.role.entity.SysPermission;
import com.example.role.server.SysPermissionServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/syspermission")
@RestController()
public class SysPermissionController {
    @Resource
    private SysPermissionServer sysPermissionserver;

    @PostMapping("/insert")
    public int insert(@RequestBody SysPermission sysPermission) {
        return sysPermissionserver.insert(sysPermission);
    }

    @PostMapping("/update")
    public int update(@RequestBody SysPermission sysPermission) {
        return sysPermissionserver.update(sysPermission);
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return sysPermissionserver.delete(id);
    }

    @GetMapping("/findAll")
    public R findAll() {
        return R.ok(sysPermissionserver.findAll());
    }

    @GetMapping("/findById/{id}")
    public SysPermission findById(@PathVariable Integer id) {
        return sysPermissionserver.findById(id);
    }
}