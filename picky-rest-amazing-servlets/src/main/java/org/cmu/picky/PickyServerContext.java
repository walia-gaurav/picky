package org.cmu.picky;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.filters.LoginFilter;
import org.cmu.picky.model.Photo;
import org.cmu.picky.services.*;
import org.cmu.picky.servlets.*;
import org.cmu.picky.util.LoggingConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class PickyServerContext implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(PickyServerContext.class);

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
        TokenService tokenService = new TokenService();
        UserService userService = new UserService(tokenService);
        PhotoService photoService = new PhotoService(tokenService);
        AuthService authService = new AuthService(userService);
        PickyService pickyService = new PickyService();
        LoginFilter.init(authService);
        LoginServlet.init(userService);
        LogoutServlet.init(authService, userService);
        SignUpServlet.init(userService);
        MyPickiesServlet.init(authService, pickyService);
        TimelineServlet.init(pickyService);
        UploadServlet.init(photoService);
        MySQLConnectionFactory.init(boneCPConfigProperties);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.gc();
        java.beans.Introspector.flushCaches();
    }

}
