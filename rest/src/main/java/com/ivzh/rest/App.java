package com.ivzh.rest;

import com.ivzh.rest.dao.UserDAO;
import com.ivzh.rest.resources.ClientResources;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import org.slf4j.LoggerFactory;

public class App extends Application<AppConfiguration> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AppConfiguration config) {
                return config.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(AppConfiguration c, Environment e) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(e, c.getDataSourceFactory(), "db");
        final Client client = new JerseyClientBuilder(e).build("REST Client");
        e.jersey().register(new ClientResources(client, jdbi.onDemand(UserDAO.class)));
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

}
