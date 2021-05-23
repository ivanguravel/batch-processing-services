package com.ivzh.batchprocessing.configs;

import com.ivzh.batchprocessing.processors.HeadersCalculatingProcessor;
import com.ivzh.batchprocessing.tasklets.BrowserDataTasklet;
import com.ivzh.batchprocessing.utils.Consts;
import com.ivzh.batchprocessing.writters.HeadersWriter;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.List;

@Configuration
public class HeadersCalculationConfig {

    private static final String JOB_NAME = "job2";
    private static final String STEP5_NAME = "step5";
    private static final String STEP6_NAME = "step6";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier(Consts.HEADERS_READER_NAME)
    private ItemReader<Header> itemReader;
    @Autowired
    @Qualifier(STEP5_NAME)
    private Step step5;

    @Bean
    public HeadersCalculatingProcessor headersCalculatingProcessor() {
        return new HeadersCalculatingProcessor();
    }

    @Bean
    public HeadersWriter headersWriter() {
        return new HeadersWriter();
    }

    @Bean
    public BrowserDataTasklet browserDataTasklet() {
        return new BrowserDataTasklet();
    }

    @Bean(STEP5_NAME)
    public Step step5(StepBuilderFactory stepBuilderFactory, ResourcelessTransactionManager transactionManager) {
        return stepBuilderFactory.get(STEP5_NAME)
                .transactionManager(transactionManager)
                .<Header, Header>chunk(2)
                .reader(itemReader)
                .processor(headersCalculatingProcessor())
                .writer(headersWriter())
                .build();
    }

    @Bean
    public Flow flow1() {
        return new FlowBuilder<Flow>("flow1")
                .start(stepBuilderFactory
                        .get(STEP6_NAME)
                        .tasklet(browserDataTasklet()).build())
                .build();
    }

    @Bean
    public Job job2() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step5)
                .split(new SimpleAsyncTaskExecutor()).add(flow1())
                .end()
                .build();
    }
}
