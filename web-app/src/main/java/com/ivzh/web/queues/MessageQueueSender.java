package com.ivzh.web.queues;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueSender {

    @Autowired
    RabbitTemplate template;
    @Value("${queue.name}")
    private String queueName;

    public void queueDelivery(Object o) {
        template.convertAndSend(this. queueName, o.toString());
    }


    public void queueDelivery(String queueName, Object o) {
        template.convertAndSend(queueName, o.toString());
    }
}
