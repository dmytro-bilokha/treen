package com.dmytrobilokha.treen.db;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@ApplicationScoped
public class DbServicesProducer {

    @Resource(name = "jdbc/TreenDB")
    private DataSource dataSource;

    @Dependent
    @Produces
    DbQueryExecutor produceDbQueryExecutor() {
        return new DbQueryExecutor(dataSource);
    }

    @Dependent
    @Produces
    DataSource produceDbDataSource() {
        return dataSource;
    }

}
