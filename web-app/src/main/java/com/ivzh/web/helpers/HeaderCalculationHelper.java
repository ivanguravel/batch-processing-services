package com.ivzh.web.helpers;

import com.ivzh.web.queues.MessageQueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;


/**
 * This class provides functionality for calculate headers which are delivered inside current small web ui.
 * Each 5 seconds it spill the pre-calculated data inside message queue and clear @calculatedResults cache.
 */
@Component
public class HeaderCalculationHelper {




    private Queue<String> headersDeliveryQueue;
    private Map<String, Long> calculatedResults;
    private HeaderCountCalculator headersCountCalculator;
    private ScheduledExecutorService scheduledExecutorService;

    @Value("${headers.queue.name}")
    private String queueName;


    @Autowired
    private MessageQueueSender messageQueueSender;


    public void addHeader4Delivery(final String header) {
        headersDeliveryQueue.add(header);
    }

    @PostConstruct
    void init() {
        headersDeliveryQueue = new ConcurrentLinkedDeque<>();
        calculatedResults = new ConcurrentHashMap<>();

        headersCountCalculator = new HeaderCountCalculator();
        headersCountCalculator.start();


        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(new CalculatedHeadersDeliveryHelper(), 2, TimeUnit.SECONDS);
    }

    private class HeaderCountCalculator extends Thread {

        private Semaphore semaphore = new Semaphore(1);

        @Override
        public void run() {
            while (true) {
                String header;
                while (!headersDeliveryQueue.isEmpty()) {
                    header = headersDeliveryQueue.poll();
                    calculateHeaders(header);
                }
            }
        }

        void stopCalculating() {
            semaphore.tryAcquire();
        }

        void continueCalculating() {
            semaphore.release();
        }

        private void calculateHeaders(final String header) {
            calculatedResults.merge(header, 1L, Long::sum);
        }
    }

    private class CalculatedHeadersDeliveryHelper extends Thread {
        @Override
        public void run() {
            try {
                headersCountCalculator.stopCalculating();
                messageQueueSender.queueDelivery(queueName, new HashMap<>(calculatedResults));
            } finally {
                headersCountCalculator.continueCalculating();
            }
        }
    }
}
