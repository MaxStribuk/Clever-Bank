package by.home.dao.util;

import by.home.dao.api.IConnection;
import by.home.util.PropertiesUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import static by.home.util.Constant.Utils.DB_PASSWORD_PROPERTIES_NAME;
import static by.home.util.Constant.Utils.DB_URL_PROPERTIES_NAME;
import static by.home.util.Constant.Utils.DB_USERNAME_PROPERTIES_NAME;
import static by.home.util.Constant.Utils.SQL_DRIVER_PROPERTIES_NAME;

/**
 * Класс, позволяющий работать с БД.
 */
@Slf4j
public class ConnectionManager implements IConnection {

    private final ComboPooledDataSource cpds;

    /**
     * конструктор данного класса должен вызываться при старте приложения,
     * чтобы корректно настроить подключение к БД
     */
    public ConnectionManager() {
        cpds = new ComboPooledDataSource();
        loadDriver();
    }

    private void loadDriver() {
        try {
            cpds.setDriverClass(PropertiesUtil.getProperty(SQL_DRIVER_PROPERTIES_NAME));
        } catch (PropertyVetoException e) {
            log.error(e.getMessage());
        }
        cpds.setJdbcUrl(PropertiesUtil.getProperty(DB_URL_PROPERTIES_NAME));
        cpds.setUser(PropertiesUtil.getProperty(DB_USERNAME_PROPERTIES_NAME));
        cpds.setPassword(PropertiesUtil.getProperty(DB_PASSWORD_PROPERTIES_NAME));
    }

    /**
     * Данный метод вызывается для получения объекта {@link Connection},
     * с помощью которого можно взаимодействовать с БД
     *
     * @return {@link Connection}
     * @throws SQLException - если получение оъекта {@link Connection} не удалось
     */
    @Override
    public Connection open() throws SQLException {
        return cpds.getConnection();
    }

    /**
     * вы же не забываете закрывать соединение с БД?
     *
     * @throws Exception -
     */
    @Override
    public void close() throws Exception {
        cpds.close();
    }
}
