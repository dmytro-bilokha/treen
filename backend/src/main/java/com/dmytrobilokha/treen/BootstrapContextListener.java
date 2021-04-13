package com.dmytrobilokha.treen;

import com.dmytrobilokha.treen.db.DbUpdater;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BootstrapContextListener implements ServletContextListener {

    private DbUpdater dbUpdater;

    public BootstrapContextListener() {
        //proxy
    }

    @Inject
    public BootstrapContextListener(DbUpdater dbUpdater) {
       this.dbUpdater = dbUpdater;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dbUpdater.update();
    }

}
