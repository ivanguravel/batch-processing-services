package com.ivzh.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AppConfiguration extends io.dropwizard.Configuration {
	@NotNull
	private final int defaultSize;
	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();
	@Valid
	@NotNull
	public final GraphQLFactory graphql = new GraphQLFactory();

	@JsonCreator
	public AppConfiguration(@JsonProperty("defaultSize") int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory factory) {
		this.database = factory;
		this.database.setMaxSize(this.defaultSize);
		this.database.setMinSize(this.defaultSize);
		this.database.setInitialSize(this.defaultSize);
		this.database.setUser(System.getenv("DB_USER"));
		this.database.setPassword(System.getenv("DB_PASSWORD"));
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("graphql")
	public GraphQLFactory getGraphQLFactory() {
		return graphql;
	}
}
