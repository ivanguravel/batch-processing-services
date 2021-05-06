package com.ivzh.batchprocessing.tasklets;

import com.ivzh.batchprocessing.readers.RabbitmqHeadersReader;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class HeaderCalculationTasklet implements Tasklet, StepExecutionListener {


    @Autowired
    private RabbitmqHeadersReader reader;


    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
