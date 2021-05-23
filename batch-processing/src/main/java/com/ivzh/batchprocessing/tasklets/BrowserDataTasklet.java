package com.ivzh.batchprocessing.tasklets;

import com.ivzh.batchprocessing.daos.HeaderDao;
import com.ivzh.batchprocessing.notifications.DefaultNotificationGateway;
import com.ivzh.batchprocessing.utils.UserAgentInfo;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


/**
 * This class (@Tasklet) provides functionality which is related to find out browser name from headers
 * and also calculating browser usage per job.
 */
public class BrowserDataTasklet implements Tasklet {

    @Autowired
    private HeaderDao dao;
    @Autowired
    private DefaultNotificationGateway gateway;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Header> headers = dao.findAll();
        List<String> collectedBrowsers = findUsedBrowsersFromHeaders(headers);
        List<Tuple> tuples = calculateBrowserUsage(collectedBrowsers);
        String message = browserStatisticStringPresentation(tuples);

        gateway.getDefaultSimpleNotificationGateway()
                .send("admin@test.com", "browserdatatasklet@test.com", "browser usage", message);

        return RepeatStatus.FINISHED;
    }

    /**
     * Helper for finding browser name by represented header.
     *
     * @param headers list of stored headers
     *
     * @return list of browsers which are used for future calculations
     *
     */
    private List<String> findUsedBrowsersFromHeaders(List<Header> headers) {
        List<String> browsers = new LinkedList<>();
        for (Header header : headers) {
            browsers.add(new UserAgentInfo(header.getName()).getBrowser());
        }
        return browsers;
    }

    /**
     * Calculator for browser usage statistic.
     *
     * @param browsers
     *
     * @return list of tuples (KV pair: browser name -> usage count)
     *
     */
    private List<Tuple> calculateBrowserUsage(List<String> browsers) {
        return browsers
            .stream()
            .map(browser -> new Tuple(browser, 1)).collect(groupingBy(Tuple::getFirst))
            .entrySet()
            .stream()
            .map(e ->
                    new Tuple(e.getKey(),
                            e.getValue().stream().map(entry -> entry.second).reduce((one, two) -> one + two).orElse(0)))
            .collect(Collectors.toList());
    }

    /**
     * Transformer of tuples with browser statistic usage to string
     *
     * @param tuples with browser statistic usage
     *
     * @return string with browser statistic usage
     *
     */
    private String browserStatisticStringPresentation(List<Tuple> tuples) {
        StringBuilder result = new StringBuilder();

        for (Tuple tuple : tuples) {
            result.append(String.format("%s %d \n", tuple.first, tuple.second));
        }

        return result.toString();
    }

    static final class Tuple {
        String first;
        int second;

        public Tuple(String first, int second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }
    }
}
