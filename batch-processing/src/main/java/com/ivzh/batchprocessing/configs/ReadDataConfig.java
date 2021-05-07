package com.ivzh.batchprocessing.configs;


import com.ivzh.batchprocessing.dtos.Header;
import com.ivzh.batchprocessing.readers.RabbitmqBatchReader;
import com.ivzh.batchprocessing.readers.RabbitmqHeadersReader;
import com.ivzh.batchprocessing.utils.Consts;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ReadDataConfig {

    @Value("${queue.name}")
    private String queueName;
    @Autowired
    private ConnectionFactory rabbitConnectionFactory;


    @Bean(Consts.READER_NAME)
    public ItemReader<String> reader() {
        return new RabbitmqBatchReader();
    }

    @Bean(Consts.HEADERS_READER_NAME)
    public ItemReader<Header> headerItemReader() { return new RabbitmqHeadersReader(); }

    @Bean
    public RabbitTemplate template() {
        RabbitTemplate template = new RabbitTemplate(this.rabbitConnectionFactory);

        template.setDefaultReceiveQueue(queueName);
        return template;
    }
}
