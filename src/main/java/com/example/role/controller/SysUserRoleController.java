package com.example.role.controller;


import com.example.role.entity.SysUserRole;
import com.example.role.server.SysUserRoleServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RequestMapping("/sysuserrole")
@RestController()
public class SysUserRoleController {
    @Resource
    private SysUserRoleServer sysUserRoleserver;

    @PostMapping("/insert")
    public int insert(@RequestBody SysUserRole sysUserRole) {
        return sysUserRoleserver.insert(sysUserRole);
    }

    @PostMapping("/update")
    public int update(@RequestBody SysUserRole sysUserRole) {
        return sysUserRoleserver.update(sysUserRole);
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return sysUserRoleserver.delete(id);
    }

    @GetMapping("/findAll")
    public List<SysUserRole> findAll() {
        return sysUserRoleserver.findAll();
    }

    @GetMapping("/findById/{id}")
    public SysUserRole findById(@PathVariable Integer id) {
        return sysUserRoleserver.findById(id);
    }
}