package com.example.role.server.impl;

import com.example.role.commons.CommonConst;
import com.example.role.commons.jwt.TokenUtil;
import com.example.role.commons.util.R;
import com.example.role.commons.util.redis.RedisCacheClient;
import com.example.role.server.CommonServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;




@Service
public class CommonServerImpl implements CommonServer {
    @Resource
    private RedisCacheClient redisCacheClient;
    @Override
    public R checkToken(HttpServletRequest rq, String tokenName) {
        String token = rq.getHeader(tokenName);
        Assert.notNull(token, CommonConst.NOT_LOGIN_MSG);
        String uid = TokenUtil.getInstance().getTokenDataOpenId(token);
        if(TokenUtil.getInstance().tokenExpire(token)){
            return R.fail(CommonConst.TOKEN_EXPIRE_MSG);
        }
        if(!isNotNullToken(uid)){
            return R.fail(CommonConst.NOT_LOGIN_MSG);
        }

        return null;
    }
    private boolean isNotNullToken(String id){
        String key = CommonConst.REDIS_TOKEN_KEY+id;
        String s = redisCacheClient.get(key);
        return !ObjectUtils.isEmpty(s);
    }
}
