package com.wll.test.zookeeper.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by wll on 17-7-12.
 */
public class StartProducer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath*:dubbo-producer.xml"});
        context.start();
        System.out.println("dubbo service begin to start");
        try {
            System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
