package org.cmu.picky;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.filters.security.AuthFilter;
import org.cmu.picky.resources.UserResource;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.UserService;
import org.cmu.picky.util.LoggingConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class PickyServerContext implements ServletContextListener {

    private final static Logger logger = LoggerFactory.getLogger(PickyServerContext.class);

    public static String contextPath;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LoggingConfigurator.configureFor(LoggingConfigurator.Environment.DEVELOPMENT);
        contextPath = servletContextEvent.getServletContext().getContextPath();

        Properties boneCPConfigProperties = new Properties();

        try {
            boneCPConfigProperties.load(PickyServerContext.class.getResourceAsStream("/bonecp.properties"));
        } catch (IOException ex) {
            logger.error("Problem reading properties", ex);
        }
        UserService userService = new UserService();
        AuthService authService = new AuthService(userService);
        AuthFilter.init(authService);
        UserResource.init(userService);
        MySQLConnectionFactory.init(boneCPConfigProperties);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.gc();
        java.beans.Introspector.flushCaches();
    }

}
