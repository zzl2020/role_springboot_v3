package com.example.role.config;


import com.example.role.commons.util.redis.RedisCacheClient;
import com.example.role.interceptor.LoginInterceptor;
import com.example.role.interceptor.RefreshInterceptor;
import com.example.role.interceptor.RoleCheckInterceptor;
import com.example.role.server.CommonServer;
import com.example.role.server.SysUserServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private RedisCacheClient redisCacheClient;
    @Resource
    private CommonServer commonServer;
    @Resource
    private SysUserServer sysUserServer;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("拦截器正在注册...");
        registry.addInterceptor(new RoleCheckInterceptor(redisCacheClient,sysUserServer))
                .excludePathPatterns(
                        "/logout",
                        "/syspermission/findAll",
                        "/sysrole/userAndRoleAndPer"
                ).order(1);
        registry.addInterceptor(new LoginInterceptor(commonServer))
                .excludePathPatterns(
                        "/login/**").order(2);
        registry.addInterceptor(new RefreshInterceptor(redisCacheClient)).order(0);
        log.info("拦截器注册成功");
    }
}
