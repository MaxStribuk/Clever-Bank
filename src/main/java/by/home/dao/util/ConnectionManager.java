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

@Slf4j
public class ConnectionManager implements IConnection {

    private final ComboPooledDataSource cpds;

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

    @Override
    public Connection open() throws SQLException {
        return cpds.getConnection();
    }

    @Override
    public void close() throws Exception {
        cpds.close();
    }
}
