package com.ivzh.batchprocessing.readers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitmqBatchReader extends AbstractStringReader {

    @RabbitListener(queues = "test")
    public void processQueue(String message) {
        deque.add(message);
    }
}
