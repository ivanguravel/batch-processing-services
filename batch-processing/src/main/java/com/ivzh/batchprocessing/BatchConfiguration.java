package com.ivzh.batchprocessing;

import com.ivzh.batchprocessing.dtos.User;
import com.ivzh.batchprocessing.processors.BlackListFilteringProcessor;
import com.ivzh.batchprocessing.processors.PersonItemProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.amqp.AmqpItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
@EnableScheduling
public class BatchConfiguration {

    private static final String JOB_NAME = "job";
    private static final String STEP_NAME = "step1";


    @Value("${queue.name}")
    private String queueName;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ConnectionFactory rabbitConnectionFactory;
    @Autowired
    private JobCompletionNotificationListener listener;
    @Autowired
    private Step step1;


	@Bean
	public ItemReader<String> reader() {
		return new AmqpItemReader<>(this.template());
	}

    @Bean
    public CompositeItemProcessor<String, ? extends User> processor() {
        final CompositeItemProcessor<String, User> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(new PersonItemProcessor(), new BlackListFilteringProcessor()));
        return processor;
    }

    @Bean
    public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO user (first_name, last_name) VALUES (:firstName, :lastName)")
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
    public Step step1(JdbcBatchItemWriter<User> writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<String, User>chunk(3)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public RabbitTemplate template() {
        RabbitTemplate template = new RabbitTemplate(this.rabbitConnectionFactory);

        template.setDefaultReceiveQueue(queueName);
        return template;
    }
}
