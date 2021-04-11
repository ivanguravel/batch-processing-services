package com.ivzh.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JobScheduler {

    private boolean enableRun = false;

    @Autowired
    private Job job;
    @Autowired
    JobLauncher jobLauncher;

    @Scheduled(fixedRate = 5_000)
    public void launchJob() throws Exception {
        if (enableRun) {
            jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
        } else {
            this.enableRun = true;
        }
    }
}
