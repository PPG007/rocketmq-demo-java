package org.example;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author koston
 */
public class Main {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("test");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.2.147:9876");
        // 启动Producer实例
        producer.start();
        String topic = "FilterTest";
        List<Message> messages = new ArrayList<>();
        String[] levels = {"A", "B", "C"};
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            Message message = new Message(topic, String.format("Hello world %d", i).getBytes());
            message.putUserProperty("Level", levels[random.nextInt(3)]);
            messages.add(message);
        }
        messages.forEach(message -> {
            System.out.println(message.getTags());
        });
        ListSplitter listSplitter = new ListSplitter(messages);
        while (listSplitter.hasNext()) {
            try {
                producer.send(listSplitter.next());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }
}