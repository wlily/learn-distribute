package com.wll.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wll on 17-7-4.
 */
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public void removeKeys(final String pattern){
        Set keys = redisTemplate.keys(pattern);
        if(keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    public void removeValue(final String key){
        if(exists(key)){
            redisTemplate.delete(key);
        }
    }

    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean set(final String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        catch (Throwable t){
            t.printStackTrace();
            return false;
        }
    }

    public boolean set(final String key, Object value, long expireTime){
        try{
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return true;
        }
        catch (Throwable t){
            t.printStackTrace();
            return false;
        }
    }

    public void setList(String key, List<?> value) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, value);
    }

    public Object getList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public void setSet(String key, Set<?> value) {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value);
    }

    public Object getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void setHash(String key, Map<String, ?> value) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
    }

    public Object getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
