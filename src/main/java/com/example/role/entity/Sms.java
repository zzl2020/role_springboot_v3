package com.example.role.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class Sms {
    private String accessKeyId;
    private String accessKeySecret;
    private String dnsName;
    private String signName;//模板名称
    private String templateCode;//模板签名
}
