package org.cmu.picky;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.filters.LoginFilter;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.PickyService;
import org.cmu.picky.services.UserService;
import org.cmu.picky.servlets.LoginServlet;
import org.cmu.picky.servlets.LogoutServlet;
import org.cmu.picky.servlets.TimelineServlet;
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
        PickyService pickyService = new PickyService();
        LoginFilter.init(authService);
        LoginServlet.init(userService);
        LogoutServlet.init(authService, userService);
        TimelineServlet.init(authService, pickyService);
        MySQLConnectionFactory.init(boneCPConfigProperties);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.gc();
        java.beans.Introspector.flushCaches();
    }

}
