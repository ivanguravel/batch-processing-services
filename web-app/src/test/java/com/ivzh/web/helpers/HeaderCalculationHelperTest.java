package com.ivzh.web.helpers;

import com.ivzh.shared.dtos.Header;
import com.ivzh.web.queues.MessageQueueSender;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Queue;

public class HeaderCalculationHelperTest {


    private static final String CHROME_HEADER = "Chrome";

    private HeaderCalculationHelper headerCalculationHelper = new HeaderCalculationHelper();
    private MessageDeliveryQueueSenderStub deliveryQueueSenderStub = new MessageDeliveryQueueSenderStub();

    @Test
    public void simpleTest() throws Exception {

        headerCalculationHelper.init();
        initStub();

        for (int i = 0; i < 4; i++) {
            headerCalculationHelper.addHeader4Delivery(CHROME_HEADER);
        }

        Queue<Header> queue = deliveryQueueSenderStub.getQueue();
        long endWaitingTime = System.currentTimeMillis() + 300_000;
        long currentTime = System.currentTimeMillis();

        while (queue.isEmpty() && currentTime <= endWaitingTime) {
            Thread.sleep(2_000);
            currentTime = System.currentTimeMillis();
        }

        Header header = queue.poll();
        boolean result = header != null && CHROME_HEADER.equals(header.getName()) && header.getCount() >= 3L;

        Assert.assertTrue(result);
    }

    private void initStub() throws Exception {
        Field declaredField = headerCalculationHelper.getClass().getDeclaredField("messageQueueSender");
        declaredField.setAccessible(true);
        declaredField.set(headerCalculationHelper, deliveryQueueSenderStub);
    }

    private static class MessageDeliveryQueueSenderStub extends MessageQueueSender {
        private Queue<Header> queue = new LinkedList<>();

        @Override
        public void queueDelivery(String queueName, Object o) {
            queue.add((Header) o);
        }

        public Queue<Header> getQueue() {
            return queue;
        }
    }
}
