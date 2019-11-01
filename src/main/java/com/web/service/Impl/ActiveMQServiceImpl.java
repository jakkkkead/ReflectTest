package com.web.service.Impl;

import com.web.service.ActiveMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import javax.jms.Topic;
import java.util.HashMap;
import java.util.Map;

//@Service
public class ActiveMQServiceImpl implements ActiveMQService {
    @Autowired
    private JmsMessagingTemplate jms;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @Override
    public void sendQueMessage() {
        for (int i=0; i< 10; i++){
            Map<String,String> map = new HashMap<>(16);
            map.put(i+"","queue"+i);
            /**
             * 队列模式，一个消息，只能别一个消费者消费
             */
            jms.convertAndSend(queue,map);
        }
    }


    @Override
    public void sendTopicMessage() {
        for (int i=0; i< 10; i++){
            /**
             * 订阅模式，多个生产者，多个消费者，意味着一个消息要被所有订阅者消费
             */
            jms.convertAndSend(topic,"topic"+i);
        }
    }
}
