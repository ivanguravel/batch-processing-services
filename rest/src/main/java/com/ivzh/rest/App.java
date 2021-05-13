package com.ivzh.rest;

import com.ivzh.rest.dao.UserDAO;
import com.ivzh.rest.dtos.User;
import com.ivzh.rest.dtos.UserDataFetcher;
import com.ivzh.rest.healthchecks.ApplicationHealthCheck;
import com.ivzh.rest.healthchecks.DatabaseHealthCheck;
import com.smoketurner.dropwizard.graphql.GraphQLBundle;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import graphql.schema.idl.RuntimeWiring;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.Arrays;
import java.util.EnumSet;


public class App extends Application<AppConfiguration> {

    private UserDataFetcher userDataFetcher;

    public static void main(String[] args) throws Exception {
        new App().run("server", "start-config.yaml");
    }

    @Override
    public void run(AppConfiguration basicConfiguration, Environment environment) {

        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, basicConfiguration.getDataSourceFactory(), "mysqlConfig");
        UserDAO dao = jdbi.onDemand(UserDAO.class);
        userDataFetcher.setDao(dao);

        int defaultSize = basicConfiguration.getDefaultSize();


        // Enable CORS to allow GraphiQL on a separate port to reach the API
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("cors", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


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


        final GraphQLBundle<AppConfiguration> bundle =
                new GraphQLBundle<AppConfiguration>() {
                    @Override
                    public GraphQLFactory getGraphQLFactory(AppConfiguration configuration) {

                        final GraphQLFactory factory = configuration.getGraphQLFactory();
                        // the RuntimeWiring must be configured prior to the run()
                        // methods being called so the schema is connected properly.
                        factory.setRuntimeWiring(buildWiring(configuration));
                        return factory;
                    }
                };
        bootstrap.addBundle(bundle);

        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.htm", "graphql-playground"));
    }


    private RuntimeWiring buildWiring(AppConfiguration configuration) {
        userDataFetcher = new UserDataFetcher();

        RuntimeWiring wiring =
                RuntimeWiring.newRuntimeWiring()
                        .type("Query", typeWiring -> typeWiring.dataFetcher("user", userDataFetcher))
                        .build();

        return wiring;
    }
}
