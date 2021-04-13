package com.dmytrobilokha.treen.db;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@ApplicationScoped
public class DbUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbUpdater.class);

    private DataSource dataSource;

    DbUpdater() {
        //Constructor required to make DbUpdater proxable
    }

    @Inject
    public DbUpdater(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update() {
        LOGGER.info("Initializing flyway DB migration");
        var flyway = new FluentConfiguration()
                .dataSource(dataSource)
                .locations("classpath:db-script")
                .baselineVersion("0.0.0")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }

}
