package com.example.role.interceptor;


import com.example.role.commons.CommonConst;
import com.example.role.commons.jwt.TokenUtil;
import com.example.role.commons.jwt.entity.Token;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.commons.util.redis.RedisCacheClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.concurrent.TimeUnit;
//刷新token拦截器
@Slf4j
public class RefreshInterceptor implements HandlerInterceptor {
    private RedisCacheClient redisCacheClient;
    public RefreshInterceptor(RedisCacheClient redisCacheClient){
        this.redisCacheClient = redisCacheClient;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(CommonConst.TOKEN_NAME);
        //判断token是否为空
        if (ObjectUtils.isEmpty(token)) return  true;

        Token tokenData = TokenUtil.getInstance().getTokenData(token);
        String tokenKey = CommonConst.REDIS_TOKEN_KEY + tokenData.getOpenId();
        boolean key = redisCacheClient.isKey(tokenKey);
        if(key) return true;

        //判断token是否过期
        boolean expire = TokenUtil.getInstance().tokenExpire(token);
        if (expire){
            String tokenStr = redisCacheClient.get(tokenKey);
            if (!tokenStr.equals(token)) throw  new CustomException(500,"请勿恶意刷token");
            //更新token
            String newToken = TokenUtil.getInstance().creatToken(tokenData.getOpenId(), tokenData.getRole());
            redisCacheClient.set(CommonConst.REDIS_TOKEN_KEY,tokenData.getOpenId(),newToken,CommonConst.REDIS_TOKEN_EXPIRE,TimeUnit.SECONDS);
            return true;
        }
        redisCacheClient.getStringRedisTemplate().expire(tokenKey,CommonConst.REDIS_TOKEN_EXPIRE*60,TimeUnit.SECONDS);
        return true;
    }
}
