package com.example.role.commons.util.redis;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class RedisCacheClient {
    //定义线程池,设置10个线程
    private static  final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 判断key是否存在
     * */
    public boolean isKey(String key){
       return BooleanUtil.isFalse(getStringRedisTemplate().hasKey(key));
    }
    /**
     * 通过key删除缓存
     * */
    public void del(String key){
        stringRedisTemplate.delete(key);
    }
    public StringRedisTemplate getStringRedisTemplate(){
       return stringRedisTemplate;
    }
    //获取操作stringRedis的对象
    public ValueOperations<String, String> getValueOps(){
        return   stringRedisTemplate.opsForValue();
    }
    //添加缓存,如果key值存在,缓存添加失败,不设置key过期时间
    public boolean setNx(String key, Object data){
        Boolean aBoolean = this.getValueOps().setIfAbsent(key, JSONUtil.toJsonStr(data));
        return BooleanUtil.isTrue(aBoolean);
    }
    //添加缓存,如果key值存在,缓存添加失败,设置key过期时间
    public boolean setNx(String key, Object data, Long time, TimeUnit unit){
        Boolean aBoolean = this.getValueOps().setIfAbsent(key, JSONUtil.toJsonStr(data), time, unit);
        return BooleanUtil.isTrue(aBoolean);
    }
    //添加缓存,如果key值存在,则更新数据,不设置key过期时间
    public void set(String preFix, Object id, Object data){
        String cacheKey = preFix+id;
        if(data instanceof  String || data instanceof Integer){
            this.getValueOps().set(cacheKey,String.valueOf(data));
        }else {
            this.getValueOps().set(cacheKey, JSONUtil.toJsonStr(data));
        }
    }
    //添加缓存,如果key值存在,则更新数据,设置key过期时间
    public  void  set(String preFix, Object id, Object data, Long time, TimeUnit unit){
        String cacheKey = preFix+id;
        this.getValueOps().set(cacheKey, JSONUtil.toJsonStr(data),time,unit);
    }
    //设置逻辑过期时间,如果key存在,则更新数据
    public void setLogicalExpire(String preFix, Object id, Object data,Long time, TimeUnit unit){
        RedisData redisData = new RedisData();
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        redisData.setData(data);
       this.set(preFix,id, JSONUtil.toJsonStr(redisData));
    }

    //解决缓存穿透工具类
    public <R,ID> R queryWithPassThrough(String preFix, ID id, Class<R> type, Function<ID,R> dbFunc,Long time,TimeUnit unit){
        String cacheShopKey= preFix+id;
        String cacheStr =this.get(cacheShopKey);
        R r = null;
        if (!StringUtils.isEmpty(cacheStr)) {
            r = JSONUtil.toBean(cacheStr, type);
            return r;
        }
        r = dbFunc.apply(id);
        if(r == null){
            this.set(preFix,id,null,time,unit);
            return null;
        }
        this.set(preFix,id,r,time,unit);
        return r;
    }

    //互斥锁解决缓存击穿
    public  <R,ID>  R queryWithMutexExpire(String preFix,ID id,Class<R> type,Function<ID,R> dbFunc,Long time,TimeUnit unit){
        String cacheShopKey= preFix+id;
        String cacheStr = this.get(cacheShopKey);
        R r = null;

        if (!StringUtils.isEmpty(cacheStr)) {
            r = JSONUtil.toBean(cacheStr, type);
            return r;
        }

        String lockKey=cacheShopKey + ":lock";
        try {
            //上锁
            boolean b = tryLock(lockKey);
            if(!b){
                //解决缓存击穿的问题
                Thread.sleep(500);
                queryWithMutexExpire(preFix,id,type,dbFunc,time,unit);
            }
            r = dbFunc.apply(id);
            this.set(preFix,id,JSONUtil.toJsonStr(r),time,unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //解锁
            unLock(lockKey);
        }
        return r;
    }

    //逻辑过期时间解决缓存击穿的问题
    public <R,ID> R queryWithLogicalExpire(String preFix, ID id, Class<R> type, Function<ID,R> dbFunc,Long time,TimeUnit unit){
        String cacheShopKey= preFix+id;
        String cacheStr = this.get(cacheShopKey);
        R r = null;
        if (StringUtils.isEmpty(cacheStr)) {
            //缓存未命中,证明这个key不是热点key,直接返回空的对象即可
            return null;
        }
        //缓存命中,判断时间是否过期
        RedisData redisData = JSONUtil.toBean(cacheStr,RedisData.class);
        r = this.getRedisLogicalData(redisData,type);
        if(redisData.getExpireTime().isAfter(LocalDateTime.now())){
            //时间未过期,直接返回缓存中的数据

            return r;
        }
        //时间已过期,定义锁的key
        String lockKey=cacheShopKey + ":lock:"+id;
        //尝试获取锁
        boolean isLock = tryLock(lockKey);
        if (isLock){
            //开启独立的线程,更新缓存数据
            CACHE_REBUILD_EXECUTOR.execute(()->{
                //查询数据库,更新缓存
                try {
                    //查询数据库,重建缓存数据
                    R r1 = dbFunc.apply(id);
                    this.setLogicalExpire(preFix,id,r1,time,unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
        //获取锁失败,返回原来的数据
        return r;
    }
    //获取key的值
    public String get(String key){
       return this.getValueOps().get(key);
    }
    //解析RedisData类型中data的值
    private  <R> R getRedisLogicalData(RedisData redisData,Class<R> type){
        return JSONUtil.toBean((JSONObject) redisData.getData(),type);
    }
    //上锁
    private boolean tryLock(String key){

        return this.setNx(key,"1", RedisConstant.LOCK_EXPIRE,TimeUnit.SECONDS);
    }
    //解锁
    private void unLock(String key){
        this.del(key);
    }



}
