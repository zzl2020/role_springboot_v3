package com.example.role.interceptor;


import com.example.role.commons.CommonConst;
import com.example.role.commons.util.R;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.server.CommonServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;




public class LoginInterceptor implements HandlerInterceptor {
    private CommonServer commonServer;

    public LoginInterceptor(CommonServer commonServer) {
        this.commonServer= commonServer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        R r = commonServer.checkToken(request, CommonConst.TOKEN_NAME);
        if(!ObjectUtils.isEmpty(r)){
            throw new CustomException(r.getCode(),r.getMsg());
        }
        return true;
    }
}
