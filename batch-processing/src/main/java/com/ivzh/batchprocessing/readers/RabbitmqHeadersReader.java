package com.ivzh.batchprocessing.readers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitmqHeadersReader extends AbstractStringReader {

    @RabbitListener(queues = "headers")
    public void processQueue(String message) {
        deque.add(message);
    }
}
