package by.home.dao.factory;

import by.home.dao.api.IConnection;
import by.home.dao.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionSingleton {

    private static volatile IConnection instance;

    public static IConnection getInstance() {
        if (instance == null) {
            synchronized (ConnectionSingleton.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }
}
