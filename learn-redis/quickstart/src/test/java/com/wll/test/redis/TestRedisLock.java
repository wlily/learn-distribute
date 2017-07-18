package com.wll.test.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wll on 17-7-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:applicationContext.xml"})
public class TestRedisLock {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
//      RedisLock1 lock = new RedisLock1();
        final RedisLock2 lock = new RedisLock2(redisTemplate, "testLock2");

        Runnable t = () -> {
            try {
                lock.lock();
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unLock();
        };

        Thread th1 = new Thread(t);
        Thread th2 = new Thread(t);
        Thread th3 = new Thread(t);

        th1.start();
        th2.start();
        th3.start();

        try {
            th1.join();
            th2.join();
            th3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
