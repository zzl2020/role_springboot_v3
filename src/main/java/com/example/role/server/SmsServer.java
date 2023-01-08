package com.example.role.server;

public interface SmsServer {
    boolean sendMsg(String phone,String code) throws Exception;
}
