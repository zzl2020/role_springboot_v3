package com.example.role.controller;


import com.example.role.aspect.WebLog;
import com.example.role.commons.util.R;
import com.example.role.entity.SysRole;
import com.example.role.entity.dto.RoleDto;
import com.example.role.server.SysRoleServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("/sysrole")
@RestController()
public class SysRoleController {
    @Resource
    private SysRoleServer sysRoleserver;

    @PostMapping("/insert")
    public int insert(@RequestBody SysRole sysRole) {
        return sysRoleserver.insert(sysRole);
    }

    @PostMapping("/update")
    public int update(@RequestBody SysRole sysRole) {
        return sysRoleserver.update(sysRole);
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return sysRoleserver.delete(id);
    }

    @GetMapping("/findAll")
    public List<SysRole> findAll() {
        return sysRoleserver.findAll();
    }

    @GetMapping("/findById/{id}")
    public SysRole findById(@PathVariable Integer id) {
        return sysRoleserver.findById(id);
    }
    @WebLog(desc = "赋权限接口")
    @PostMapping("/roleAddPermission")
    public R roleAddPermission(@RequestBody RoleDto roleDto) {
        return sysRoleserver.roleAddPermission( roleDto);
    }
    @WebLog(desc = "取消权限接口")
    @PostMapping("/cancel")
    public R cancelPermission( @RequestBody List<Integer> ids) {
        return sysRoleserver.cancelPermission( ids);
    }
    @WebLog(desc = "根据角色获取权限接口")
    @GetMapping("/rolePermission")
    public R getRolePermission(@PathVariable Integer id){
        return R.ok(sysRoleserver.getRolePermission(id));
    }
}