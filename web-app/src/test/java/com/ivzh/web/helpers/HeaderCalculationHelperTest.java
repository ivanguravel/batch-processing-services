package com.ivzh.web.helpers;

import com.ivzh.web.queues.MessageQueueSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class HeaderCalculationHelperTest {

    private HeaderCalculationHelper headerCalculationHelper = new HeaderCalculationHelper();


    @BeforeAll
    private void before() {

    }


    @Test
    public void simpleTest() throws InterruptedException {
        headerCalculationHelper.addHeader4Delivery("Chrome");


    }

    private static class MessageDeliveryQueueSenderStub extends MessageQueueSender {
        private Queue<Map<String, Long>> queue = new LinkedList<>();


        //public void
    }
}
