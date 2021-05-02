package com.ivzh.batchprocessing.readers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.ItemReader;
import org.springframework.util.Assert;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;


public class RabbitmqBatchReader implements ItemReader<String> {

    private final Deque<String> deque;


    public RabbitmqBatchReader() {
        this.deque = new ConcurrentLinkedDeque<>();
    }

    @Override
    public String read() throws Exception {
        return deque.isEmpty() ? "" : deque.poll();
    }

    @RabbitListener(queues = "test")
    public void processQueue1(String message) {
        deque.add(message);
    }
}
