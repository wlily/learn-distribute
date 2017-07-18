package com.wll.test.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * Created by wll on 17-7-11.
 */
public class RedisLock1 {
    private String LOCK_KEY = "mylock_";
    private int EXPIRED_TIME = 60*1000;

    private Jedis getConnection(){
        Jedis jedis = new Jedis("localhost");
//        System.out.println("Connection to server sucessfully");
        return jedis;
    }
    /**
     *锁在给定等待时间内空闲，则获取锁成功， 返回true， 否则返回false
     */
    public boolean lock(long timeout, TimeUnit unit){
        String key = LOCK_KEY + "_test";
//        System.out.println(Thread.currentThread().getName() + " try lock key: " + key);

        Jedis jedis = getConnection();
        if(jedis == null){
            return Boolean.FALSE;
        }
        long nano = System.nanoTime();
        do{
            long i = jedis.setnx(key, key);
            if(i == 1){
                jedis.expire(key, EXPIRED_TIME);
                System.out.println(Thread.currentThread().getName()+ " get lock, key: " + key + ", expire in " + EXPIRED_TIME + " seconds");
                return true;
            }
            else{
//                System.out.println("key: " + key + " locked by another business: " + jedis.get(key));
            }

            if(timeout == 0){
                //取不到锁，不等待，直接返回
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        while((System.nanoTime() - nano) < unit.toNanos(timeout));
        return false;
    }

    /**
     *如果锁空闲，立即返回，如果获取失败，则一直等待
     */
    public boolean lock(){
        String key = LOCK_KEY + "_test";
//        System.out.println("try lock key: " + key);

        Jedis jedis = getConnection();
        if(jedis == null){
            return false;
        }

        while(true) {
            long i = jedis.setnx(key, key);
            if (i == 1) {
                jedis.expire(key, EXPIRED_TIME);
                System.out.println(Thread.currentThread().getName() + " get lock, key: " + key + ", expire in " + EXPIRED_TIME + " seconds");
                return true;
            } else {
//                System.out.println("key: " + key + " locked by another business: " + jedis.get(key));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }

    public void unLock(){
        String key = LOCK_KEY + "_test";
        Jedis jedis = getConnection();
        if(jedis == null){
            return;
        }
        jedis.del(key);
        System.out.println(Thread.currentThread().getName() + " release lock, keys: " + key);
    }
}
