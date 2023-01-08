package com.example.role.controller;


import com.example.role.aspect.WebLog;
import com.example.role.commons.util.R;
import com.example.role.entity.SysUser;
import com.example.role.entity.dto.UpdateUserDto;
import com.example.role.entity.dto.UserDto;
import com.example.role.server.SysUserServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sysuser")
@RestController()
public class SysUserController {

    @Resource
    private SysUserServer sysUserServer;

    @PostMapping("/insert")
    public int insert(@RequestBody SysUser sysUser) {
        return sysUserServer.insert(sysUser);
    }

    @PostMapping("/update")
    public R update(@RequestBody UpdateUserDto updateUserDto) {
        return sysUserServer.update(updateUserDto);
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return sysUserServer.delete(id);
    }

    @GetMapping("/findAll")
    public List<SysUser> findAll() {
        return sysUserServer.findAll();
    }

    @GetMapping("/findById/{id}")
    public SysUser findById(@PathVariable Integer id) {
        return sysUserServer.findById(id);
    }
    @WebLog(desc = "添加角色接口")
    @PostMapping("/userAddRole")
    public R userAddRole( @RequestBody UserDto userDto) {

        return sysUserServer.userAddRole(userDto);
    }
    @WebLog(desc = "取消角色接口")
    @PostMapping("/cancel")
    public R cancelRole(@RequestBody List<Integer> ids){
        return sysUserServer.cancelRole(ids);
    }
    @GetMapping("/userAndRoleAndPer/{userName}")
    public R getUserOrRole(@PathVariable("userName")String userName){
        return sysUserServer.getUserAndRoleAndPer(userName);
    }
    @WebLog(desc = "根据用户获取角色接口")
    @GetMapping("/userRole/{userName}")
    public R getUserRole(@PathVariable String userName){
        return R.ok(sysUserServer.getUserRole(userName));
    }
}