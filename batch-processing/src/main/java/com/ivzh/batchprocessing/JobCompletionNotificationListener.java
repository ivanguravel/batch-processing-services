package com.ivzh.batchprocessing;

import com.ivzh.batchprocessing.notifications.DefaultNotificationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private DefaultNotificationGateway defaultNotificationGateway;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
        } else {
            defaultNotificationGateway.getDefaultSimpleNotificationGateway()
                    .send("app@test.com", "admin@test.com", "job failed", String
							.format("job with id %d failed with status : %s", jobExecution.getJobId(), jobExecution.getStatus()));
        }
    }
}
