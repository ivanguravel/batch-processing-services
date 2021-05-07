package com.ivzh.batchprocessing.tasklets;

import com.ivzh.batchprocessing.dtos.Header;
import com.ivzh.batchprocessing.readers.RabbitmqHeadersReaderMq;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class HeaderCalculationTasklet implements Tasklet, StepExecutionListener {

    private List<Header> cache = new LinkedList<>();


    @Autowired
    private RabbitmqHeadersReaderMq reader;


    @Override
    public void beforeStep(StepExecution stepExecution) {
        int count = 0;
        while(!reader.isQueueEmpty() || count++ < 1_000) {
            cache.add(reader.poll());
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<Header> collect = cache.parallelStream()
                .collect(groupingBy(Header::getName))
                .entrySet()
                .parallelStream()
                .map(entry -> new Header(entry.getKey(), entry.getValue().stream().map(Header::getCount).reduce(Long::sum).orElse(0L)))
                .collect(Collectors.toList());


        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {


        return ExitStatus.COMPLETED;
    }
}
