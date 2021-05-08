package com.ivzh.batchprocessing.configs;


import com.ivzh.shared.dtos.User;
import com.ivzh.batchprocessing.utils.Consts;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataConfiguration {

    @Bean(Consts.MYSQL_DATA_SOURCE)
    public DataSource dataSource() {
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/TESTS?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false");
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("org/springframework/batch/core/schema-drop-mysql.sql"));
        databasePopulator.addScript(new ClassPathResource("org/springframework/batch/core/schema-mysql.sql"));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }

    @Bean
    public ResourcelessTransactionManager txManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean(Consts.SQL_PARAMETER_PROVIDER)
    public BeanPropertyItemSqlParameterSourceProvider<User> beanPropertyItemSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<>();
    }
}
