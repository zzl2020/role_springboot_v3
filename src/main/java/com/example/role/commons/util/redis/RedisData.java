package com.example.role.commons.util.redis;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
