package com.example.role.interceptor;


import com.example.role.commons.jwt.TokenUtil;
import com.example.role.commons.jwt.entity.Token;
import com.example.role.commons.util.R;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.commons.util.redis.RedisCacheClient;
import com.example.role.entity.SysPermission;
import com.example.role.entity.SysRole;
import com.example.role.entity.SysUser;
import com.example.role.server.SysUserServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleCheckInterceptor implements HandlerInterceptor {
    private RedisCacheClient redisCacheClient;
    private SysUserServer sysUserServer;
    public  RoleCheckInterceptor(RedisCacheClient redisCacheClient, SysUserServer sysUserServer){
        this.redisCacheClient =redisCacheClient;
        this.sysUserServer = sysUserServer;
    }


    @Override
    public boolean preHandle(HttpServletRequest rq, HttpServletResponse res, Object handler) throws Exception {
        String token = rq.getHeader("token");
        if (ObjectUtils.isEmpty(token)) return true;
        //获取当前用户所有权限
        Token tokenData = TokenUtil.getInstance().getTokenData(token);
        R r = sysUserServer.getUserAndRoleAndPer(tokenData.getRole());
        if(r.getCode()!=200){
            throw new CustomException(r.getCode(),r.getMsg());
        }
        Assert.notNull(r.getData(),"权限不够");
        SysUser data = (SysUser) r.getData();

        //存储当前用户所有权限
        Set<String> permissions= new HashSet<>();
        List<SysRole> roles = data.getRoles();
        roles.forEach(t->{
            List<SysPermission> permissions1 = t.getPermissions();
            permissions1.forEach(s->{
              permissions.add(s.getUrl());
            });
        });
        //获取访问的当前接口
        String requestURI = rq.getRequestURI();
        long count = permissions.stream().filter(t->!t.isEmpty()).filter(requestURI::contains).count();
        if(count==0){
            res.setStatus(401);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
