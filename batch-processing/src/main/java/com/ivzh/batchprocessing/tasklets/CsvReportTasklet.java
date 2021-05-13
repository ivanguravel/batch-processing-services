package com.ivzh.batchprocessing.tasklets;


import com.ivzh.batchprocessing.daos.UserDao;
import com.ivzh.shared.dtos.User;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Scope("step")
public class CsvReportTasklet implements Tasklet, StepExecutionListener {


    @Value("${app.csv.export.dir}")
    private String filePath;
    @Value("#{jobParameters['pageSize']}")
    private Integer pageSize;
    @Value("#{jobParameters['offset']}")
    private Integer offset;

    @Autowired
    private UserDao userDao;

    private File directoryForWritingData;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        directoryForWritingData = new File(filePath);
        FileSystemUtils.deleteRecursively(directoryForWritingData);
        directoryForWritingData.mkdir();
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        PageImpl<User> usersPage = userDao.findAll(PageRequest.of(,pageSize, offset));
        List<User> users = usersPage.get().collect(Collectors.toList());

        try (FileOutputStream out = new FileOutputStream(filePath + "report.csv");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {

            for (User user : users) {
                bufferedOutputStream
                        .write(String.join(",", user.getFirstName(), user.getLastName()).getBytes(StandardCharsets.UTF_8));
            }
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        directoryForWritingData = null;
        return ExitStatus.COMPLETED;
    }
}
