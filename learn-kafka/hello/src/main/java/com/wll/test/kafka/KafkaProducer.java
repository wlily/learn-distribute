package com.wll.test.kafka;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
/**
 * Created by wll on 17-7-19.
 */
public class KafkaProducer extends Thread{
    private String topic;

    public KafkaProducer(String topic){
        super();
        this.topic = topic;
    }


    @Override
    public void run() {
        Producer producer = createProducer();
        int i=0;
        while(true){
            producer.send(new KeyedMessage<Integer, String>(topic, "message: " + i++));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Producer createProducer() {
        Properties properties = new Properties();
        //声明zk
        properties.put("zookeeper.connect", "localhost:2181/kafka,localhost:2182/kafka,localhost:2183/kafka");
        properties.put("serializer.class", StringEncoder.class.getName());
        // 声明kafka broker ，要注意地址一定要正确
        properties.put("metadata.broker.list", "127.0.0.1:19092,127.0.0.1:29092,127.0.0.1:39092");
        return new Producer<Integer, String>(new ProducerConfig(properties));
    }


    public static void main(String[] args) {
        new KafkaProducer("test-topic").start();// 使用kafka集群中创建好的主题 test-topic
    }

}