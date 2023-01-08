package com.example.role.commons;

public class CommonConst {
    public static final String TOKEN_NAME="token";
    public static final String TOKEN_EXPIRE_MSG="token is expire";
    public static final String TOKEN_NULL_MSG="token is null";
    public static final String NOT_LOGIN_MSG="未登陆,请先登陆";
    public static final String CODE_NULL_MSG="验证码不能为空";
    public static final String CODE_ERROR_MSG="验证码错误";
    public static final String CODE_SEND_ERROR_MSG="验证码发送失败";
    public static final String PWD_ERROR_MSG="密码错误";
    public static final String USER_NULL_MSG="用户不存在";
    public static final String USER_LOCK_MSG="用户已锁定";
    public static final String ACCOUNT_NULL_MSG="账号不能为空";
    public static final String NULL_LOGIN_MSG="loginDto is null";
    public static final String ICON_NULL_MSG="头像不存在";
    public static final String REGISTER_ERROR_MSG="您已经注册过了,不能重复注册";
    public static final String UPDATE_INFO_ERROR_MSG="该账号还未注册,请先注册";
    //////////////////////////redis常量开始/////////////////////////////
    public static final String REDIS_TOKEN_KEY="user:login:";
    public static final String REDIS_VCODE_KEY="user:vcode:";
    public static final Long  REDIS_VCODE_EXPIRE=5*60L;
    public static final Long  REDIS_TOKEN_EXPIRE=30*60L;
    /////////////////////////redis常量结束////////////////////////////
    public static final Integer START_STATUS_CODE =0;
    public static final Integer STOP_STATUS_CODE =1;

    public static final String SEND_SMS_STATUS_OK="OK";

    public static final String PRINT_REQ_LOG_CLASS_NAME="PrintReqLog";

    public static final String PHONE_REGEXP ="^1[3-9]\\d{9}$";
}
