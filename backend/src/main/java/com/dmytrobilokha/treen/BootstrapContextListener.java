package com.dmytrobilokha.treen;

import com.dmytrobilokha.treen.db.DbUpdater;
import com.dmytrobilokha.treen.login.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.management.ManagementFactory;

@WebListener
public class BootstrapContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(BootstrapContextListener.class);

    private static final String AUTH_SERVICE_JMX_NAME = "treen:type=AuthenticationService";

    private DbUpdater dbUpdater;
    private AuthenticationService authenticationService;

    public BootstrapContextListener() {
        //proxy
    }

    @Inject
    public BootstrapContextListener(DbUpdater dbUpdater, AuthenticationService authenticationService) {
        this.dbUpdater = dbUpdater;
        this.authenticationService = authenticationService;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dbUpdater.update();
        registerJmxBean();
    }

    private void registerJmxBean() {
        var server = ManagementFactory.getPlatformMBeanServer();
        try {
            var objectName = new ObjectName(AUTH_SERVICE_JMX_NAME);
            server.registerMBean(authenticationService, objectName);
        } catch (MBeanRegistrationException | MalformedObjectNameException
                | InstanceAlreadyExistsException | NotCompliantMBeanException e) {
            LOG.error("Failed to register authentication service JMX bean", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        unregisterJmxBean();
    }

    private void unregisterJmxBean() {
        var server = ManagementFactory.getPlatformMBeanServer();
        try {
            var objectName = new ObjectName(AUTH_SERVICE_JMX_NAME);
            server.unregisterMBean(objectName);
        } catch (MBeanRegistrationException | MalformedObjectNameException | InstanceNotFoundException e) {
            LOG.error("Failed to unregister authentication service JMX bean", e);
        }
    }

}
