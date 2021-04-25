package com.ivzh.rest;

import com.ivzh.rest.dao.UserDAO;
import com.ivzh.rest.dtos.User;
import com.ivzh.rest.healthchecks.ApplicationHealthCheck;
import com.ivzh.rest.healthchecks.DatabaseHealthCheck;
import com.ivzh.rest.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

import java.util.Arrays;


public class App extends Application<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        new App().run("server", "start-config.yaml");
    }

    @Override
    public void run(AppConfiguration basicConfiguration, Environment environment) {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, basicConfiguration.getDataSourceFactory(), "mysqlConfig");
        final UserDAO dao = jdbi.onDemand(UserDAO.class);

        int defaultSize = basicConfiguration.getDefaultSize();
        UserResource userResource = new UserResource(defaultSize, dao);


        environment.jersey().register(userResource);

        ApplicationHealthCheck healthCheck = new ApplicationHealthCheck();
        DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(jdbi, basicConfiguration.getDataSourceFactory().getValidationQuery());
        environment
                .healthChecks()
                .register("application", healthCheck);

        environment
                .healthChecks()
                .register("db", databaseHealthCheck);
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        super.initialize(bootstrap);
    }
}
