package com.ivzh.web.helpers;

import com.ivzh.web.queues.MessageQueueSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class HeaderCalculationHelperTest {

    private HeaderCalculationHelper headerCalculationHelper = new HeaderCalculationHelper();
    private MessageDeliveryQueueSenderStub deliveryQueueSenderStub = new MessageDeliveryQueueSenderStub();

    @Test
    public void simpleTest() throws Exception {

        headerCalculationHelper.init();
        initStub();

        for (int i = 0; i < 4; i++) {
            headerCalculationHelper.addHeader4Delivery("Chrome");
        }

        Queue<Map<String, Long>> queue = deliveryQueueSenderStub.getQueue();
        long endTestTime = System.currentTimeMillis() + 300_000;
        Long currentTime = System.currentTimeMillis();

        while (queue.isEmpty() && currentTime <= endTestTime) {
            currentTime = System.currentTimeMillis();
        }

        boolean result = false;
        Map<String, Long> map = queue.poll();

        if (map != null && map.getOrDefault("Chrome", 0L) == 4) {
            result = true;
        }

        Assert.assertTrue(result);

    }

    private void initStub() throws Exception {
        Field declaredField = headerCalculationHelper.getClass().getDeclaredField("messageQueueSender");
        declaredField.setAccessible(true);
        declaredField.set(headerCalculationHelper, deliveryQueueSenderStub);
    }

    private static class MessageDeliveryQueueSenderStub extends MessageQueueSender {
        private Queue<Map<String, Long>> queue = new LinkedList<>();

        @Override
        public void queueDelivery(String queueName, Object o) {
            queue.add((Map<String, Long>) o);
        }

        public Queue<Map<String, Long>> getQueue() {
            return queue;
        }
    }
}
