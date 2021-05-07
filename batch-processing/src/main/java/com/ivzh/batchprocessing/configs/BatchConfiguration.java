package com.ivzh.batchprocessing.configs;

import com.ivzh.batchprocessing.JobCompletionNotificationListener;
import com.ivzh.batchprocessing.dtos.User;
import com.ivzh.batchprocessing.processors.BlackListFilteringProcessor;
import com.ivzh.batchprocessing.processors.PersonItemProcessor;
import com.ivzh.batchprocessing.utils.Consts;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    private static final String JOB_NAME = "job";
    private static final String STEP_NAME = "step1";




    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier(Consts.READER_NAME)
    private ItemReader<String> reader;
    @Autowired
    @Qualifier(Consts.MYSQL_DATA_SOURCE)
    private DataSource dataSource;
    @Autowired
    @Qualifier(Consts.SQL_PARAMETER_PROVIDER)
    private BeanPropertyItemSqlParameterSourceProvider<User> beanPropertyItemSqlParameterSourceProvider;
    @Autowired
    private JobCompletionNotificationListener listener;
    @Autowired
    private Step step1;




    @Bean
    public JobRepository mysqlJobRepository(DataSource dataSource, ResourcelessTransactionManager transactionManager)
            throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDatabaseType(DatabaseType.MYSQL.getProductName());
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        return factory.getObject();
    }
    @Bean
    public JobLauncher mysqljobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }

    @Bean
    public CompositeItemProcessor<String, ? extends User> processor() {
        final CompositeItemProcessor<String, User> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(new PersonItemProcessor(), new BlackListFilteringProcessor()));
        return processor;
    }

    @Bean
    public JdbcBatchItemWriter<User> writer() {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(beanPropertyItemSqlParameterSourceProvider)
                .sql("INSERT INTO users (person_id, first_name, last_name) VALUES (:id, :firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<User> writer, StepBuilderFactory stepBuilderFactory, ResourcelessTransactionManager transactionManager) {
        return stepBuilderFactory.get(STEP_NAME)
                 .transactionManager(transactionManager)
                .<String, User>chunk(3)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }


}
