package com.ivzh.batchprocessing.readers;

import com.ivzh.batchprocessing.dtos.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitmqHeadersReader extends AbstractMqReader<Header> {

    @RabbitListener(queues = "headers")
    public void processQueue(Header header) {
        deque.add(header);
    }
}
