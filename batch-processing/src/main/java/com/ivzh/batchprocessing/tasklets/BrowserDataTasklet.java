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

public class BrowserDataTasklet implements Tasklet {

    @Autowired
    private HeaderDao dao;
    @Autowired
    private DefaultNotificationGateway gateway;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Header> headers = dao.findAll();
        List<String> collectedBrowsers = new LinkedList<>();
        for (Header header : headers) {
            collectedBrowsers.add(new UserAgentInfo(header.getName()).getBrowser());
        }

        List<Tuple> tuples = collectedBrowsers
                .stream()
                .map(browser -> new Tuple(browser, 1)).collect(groupingBy(Tuple::getFirst))
                .entrySet()
                .stream()
                .map(e ->
                        new Tuple(e.getKey(),
                                e.getValue().stream().map(entry -> entry.second).reduce((one, two) -> one + two).orElse(0)))
                .collect(Collectors.toList());

        StringBuilder message = new StringBuilder();

        for (Tuple tuple : tuples) {
            message.append(String.format("%s %d \n", tuple.first, tuple.second));
        }

        gateway.getDefaultSimpleNotificationGateway()
                .send("admin@test.com", "browserdatatasklet@test.com", "browser usage", message.toString());

        return RepeatStatus.FINISHED;
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