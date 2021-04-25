package com.ivzh.rest.healthchecks;

import org.skife.jdbi.v2.DBI;
import com.codahale.metrics.health.HealthCheck;
import org.skife.jdbi.v2.Handle;

import java.util.Optional;

public class DatabaseHealthCheck  extends HealthCheck {
    private final DBI dbi;
    private final String validationQuery;

    public DatabaseHealthCheck(DBI dbi, Optional<String> validationQuery) {
        this.dbi = dbi;
        this.validationQuery = validationQuery.orElseThrow(
                () -> new IllegalArgumentException(String.format("can't handle validation for db url")));
    }

    @Override
    protected Result check() {
        Handle handle = null;
        try {
            handle = dbi.open();
            handle.execute(validationQuery);
            handle.close();
        } catch (Exception e) {
            return Result.unhealthy("Database is not running! : " + e.getMessage());
        } finally {
            handle.close();
        }
        return Result.healthy();
    }
}
