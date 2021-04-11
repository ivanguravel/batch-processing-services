package com.ivzh.rest;


import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate database connection
 */
public class DataBaseConfiguration implements DatabaseConfiguration {
    static final Logger logger = LoggerFactory.getLogger(DataBaseConfiguration.class);
    private static DatabaseConfiguration dbConfiguration;

    public static DatabaseConfiguration create(String url) {
       dbConfiguration = new DataBaseConfiguration() {
            DataSourceFactory dataSourceFactory;
                @Override
                public DataSourceFactory getDataSourceFactory(Configuration configuration){
                    DataSourceFactory factory = new DataSourceFactory();
                    factory.setUser("sa");
                    factory.setPassword("sa");
                    factory.setUrl("jdbc:hsqldb:target/example");
                    factory.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
                    dataSourceFactory = factory;
                    return factory;
                }
            };

        return dbConfiguration;
    }

    @Override
    public DataSourceFactory getDataSourceFactory(Configuration configuration){
        return dbConfiguration.getDataSourceFactory(null);
    }
}
