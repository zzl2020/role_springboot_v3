package com.example.role.server.impl;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.example.role.commons.CommonConst;
import com.example.role.commons.util.CommonUtil;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.entity.Sms;
import com.example.role.server.SmsServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class SmsServerImpl implements SmsServer {
    @Resource
    private Sms sms;
    @Override
    public boolean sendMsg(String phone, String code) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        Client client = CommonUtil.createClient(sms.getAccessKeyId(),sms.getAccessKeySecret(),sms.getDnsName());
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);//接收短信的手机号码
        request.setSignName(sms.getSignName());//短信签名名称
        request.setTemplateCode(sms.getTemplateCode());//短信模板CODE
        request.setTemplateParam(JSONObject.toJSONString(map));//短信模板变量对应的实际值
        // 复制代码运行请自行打印API的返回值
        SendSmsResponse sendSmsResponse = client.sendSms(request);
        if(!CommonConst.SEND_SMS_STATUS_OK.equals(sendSmsResponse.body.code)){
            log.error("验证码发送失败:{}",sendSmsResponse.body.message);
            throw new CustomException(500,sendSmsResponse.body.message);
        }
        return true;
    }
}
