package by.home.controller.listener;

import by.home.factory.util.ConnectionSingleton;
import by.home.factory.util.ExecutorManagerSingleton;
import by.home.factory.util.ValidationManagerSingleton;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Данный класс используется для инициализации объектов,
 * необходимых для нормального функционирования веб-приложения.
 * Явно вызывать методы данного класса нет необходимости, они будут вызваны
 * автоматически при деплое приложения. При запуске консольной версии приложения
 * данный класс не используется.
 */
@WebListener
@Slf4j
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ConnectionSingleton.getInstance();
        ExecutorManagerSingleton.getInstance().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {
            ValidationManagerSingleton.getInstance().close();
            ConnectionSingleton.getInstance().close();
            ExecutorManagerSingleton.getInstance().destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
