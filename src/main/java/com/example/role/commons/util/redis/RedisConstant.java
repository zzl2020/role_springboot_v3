package com.example.role.commons.util.redis;

public class RedisConstant {
    public static final String CODE_KEY = "phone:";
    public static final String USER_KEY = "user:";
    public static final Long CODE_EXPIRE=5L*60L;
    public static final Long USER_EXPIRE=30L*60L;
    public static final Long EXPIRE=-1L;
    public static final String CACHE_SHOP_KEY="cache:shop:";
    public static final String CACHE_SHOP_TYPE_KEY="cache:shop:type";
    public static final Long CACHE_SHOP_EXPIRE=30L;
    public static final Long LOCK_EXPIRE=10L;
    public static final String SELLER_STOCK_KEY="seller:stock:";
    public static final String ORDER_USER_KEY="oder:user:";
    public static final int ORDER_QUEUE_SIZE =1024*1024;
    public static final String BLOG_USER_KEY ="blog:user:";
    public static final String BLOG_ERR_MSG="您已点赞,不能重复点赞";

}
