package com.web.listener;

import com.web.config.ActiveMQConfig;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class ActiveMQConsumer {
    @JmsListener(destination = ActiveMQConfig.QUEUE,containerFactory = "jmsListenerContainerQueue" )
    public void getQueueMessage(Map<String,String> message){
        System.out.println("queue1接收到消息："+message);
    }
    @JmsListener(destination = ActiveMQConfig.QUEUE,containerFactory = "jmsListenerContainerQueue" )
    public void getQueue1Message(Map<String,String> message){
        System.out.println("queue2接收到消息："+message);
    }
    //topic模式的消费者，需要指定队列，连接工厂
    @JmsListener(destination = ActiveMQConfig.TOPIC,containerFactory = "jmsListenerContainerTopic")
    public void getTopicMessage(String message){
        System.out.println("topic1接收到消息："+message);
    }
    @JmsListener(destination = ActiveMQConfig.TOPIC,containerFactory = "jmsListenerContainerTopic")
    public void getTopic1Message(String message){
        System.out.println("topic2接收到消息："+message);
    }
}
