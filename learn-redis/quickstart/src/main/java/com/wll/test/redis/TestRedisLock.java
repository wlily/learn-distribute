package com.wll.test.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by wll on 17-7-11.
 */
public class TestRedisLock {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        RedisTemplate redisTemplate = (RedisTemplate)context.getBean("redisTemplate");

        final RedisLock2 lock = new RedisLock2(redisTemplate, "testLock2");
//        final RedisLock1 lock = new RedisLock1();

        Runnable t = () -> {
            try {
                boolean locked = lock.lock();
                Thread.sleep(10*1000);
                if(locked){
                    lock.unLock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread th1 = new Thread(t);
        Thread th2 = new Thread(t);
        Thread th3 = new Thread(t);

        th1.start();
        th2.start();
        th3.start();
    }
}
