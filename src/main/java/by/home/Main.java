package by.home;

import by.home.dao.factory.ConnectionSingleton;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = ConnectionSingleton.getInstance().open()) {
            System.out.println(connection.getAutoCommit());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("все ок");
    }
}