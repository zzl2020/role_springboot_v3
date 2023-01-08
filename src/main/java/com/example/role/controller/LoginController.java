package com.example.role.controller;


import com.example.role.aspect.PrintReqLog;
import com.example.role.aspect.PrintRespLog;
import com.example.role.aspect.WebLog;
import com.example.role.commons.util.R;
import com.example.role.entity.dto.LoginDto;
import com.example.role.entity.dto.RegisterDto;
import com.example.role.server.SysUserServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RequestMapping("/login")
@RestController
public class LoginController {
    @Resource
    private SysUserServer sysUserServer;

    @PostMapping("/login")
    @WebLog(desc = "登陆接口")
    public R login(@RequestBody @Valid LoginDto loginDto) {
        return sysUserServer.login(loginDto);
    }

    @WebLog(desc = "登出接口")
    @PostMapping("/logout")
    public R logout(HttpServletRequest rq) {
        return sysUserServer.logout(rq);
    }

    @WebLog(desc = "注册接口")
    @PostMapping("/register")
    public R register(@RequestBody @Valid RegisterDto registerDto) {
        return sysUserServer.register(registerDto);
    }

    @WebLog(desc = "图像上传接口")
    @PostMapping("/iconUpload")
    public R iconUpload(MultipartFile file) throws IOException {
        return sysUserServer.iconUpload(file);
    }
    @PrintReqLog
    @PrintRespLog
    @WebLog(desc = "发送验证码接口")
    @PostMapping("/sendCode/{phone}")
    public R sendCode(
                      @PathVariable
                      String phone) throws Exception {
        return sysUserServer.sendCode(phone);
    }

    @WebLog(desc = "获取头像接口")
    @GetMapping("/getIcon")
    public R getIcon(@RequestParam  String iconPath){
        return sysUserServer.getIcon(iconPath);
    }
}
