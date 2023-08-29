package by.home.dao.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnection {

    void init();

    Connection open() throws SQLException;

    void close() throws Exception;
}
