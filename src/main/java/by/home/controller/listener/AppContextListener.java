package by.home.controller.listener;

import by.home.factory.util.ConnectionSingleton;
import by.home.factory.util.ValidationManagerSingleton;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@WebListener
@Slf4j
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ConnectionSingleton.getInstance().init();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {
            ValidationManagerSingleton.getInstance().close();
            ConnectionSingleton.getInstance().close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
