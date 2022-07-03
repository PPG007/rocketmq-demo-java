package org.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Random;

/**
 * @author koston
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("test");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.2.147:9876");
        // 启动Producer实例
        producer.start();
        String[] tags = {"A", "B", "C"};
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("FilterTest" , tags[random.nextInt(3)],
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            // 发送消息到一个Broker
            SendResult sendResult = producer.send(msg);
            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}