package com.ivzh.batchprocessing.configs;

import com.ivzh.batchprocessing.tasklets.CsvReportTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskletConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    protected Step csvProcessor() {
        return steps
                .get("cscResport")
                .tasklet(csvReportTasklet())
                .build();
    }


    @Bean
    protected CsvReportTasklet csvReportTasklet() {
        return new CsvReportTasklet();
    }

    @Bean
    public Job csvJob() {
        return jobs
                .get("taskletsJob")

                .start(csvProcessor())
                .build();
    }
}
