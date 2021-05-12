package com.ivzh.web.helpers;

import com.ivzh.shared.dtos.Header;
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

    /**
     * Send data to the related message queue for future calculating headers count
     *
     * @param header which needs to be stored for future calculation
     *
     */
    public void addHeader4Delivery(final String header) {
        headersDeliveryQueue.add(header);
    }

    /**
     * Needed for pre-initialization of this class state.
     * Called from Spring framework internals.
     *
     * @see @javax.annotation.PostConstruct
     */
    @PostConstruct
    void init() {
        headersDeliveryQueue = new ConcurrentLinkedDeque<>();
        calculatedResults = new ConcurrentHashMap<>();

        headersCountCalculator = new HeaderCountCalculator();
        headersCountCalculator.start();


        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(new CalculatedHeadersDeliveryHelper(), 2, TimeUnit.SECONDS);
    }

    /**
     * This helper used for calculation headers over the requests.
     * User can stop calculation with help included Semaphore
     */
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

    /**
     * This helper used stopping calculation of headers and send current result to the message queue.
     */
    private class CalculatedHeadersDeliveryHelper extends Thread {
        @Override
        public void run() {
            try {
                headersCountCalculator.stopCalculating();
                for (Map.Entry<String, Long> e : calculatedResults.entrySet()) {
                    messageQueueSender.queueDelivery(queueName, new Header(e.getKey(), e.getValue()));
                }
                calculatedResults.clear();
            } finally {
                headersCountCalculator.continueCalculating();
            }
        }
    }
}
