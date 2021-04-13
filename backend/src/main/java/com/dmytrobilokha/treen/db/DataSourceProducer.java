package com.dmytrobilokha.treen.db;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@ApplicationScoped
public class DataSourceProducer {

    @Resource(name = "jdbc/TreenDB")
    private DataSource dataSource;

    @Dependent
    @Produces
    DataSource produce() {
        return dataSource;
    }

}
