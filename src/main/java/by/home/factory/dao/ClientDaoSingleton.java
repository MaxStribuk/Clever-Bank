package by.home.factory.dao;

import by.home.dao.api.IClientDao;
import by.home.dao.impl.ClientDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * создает единственный объект класса, реализующего {@link IClientDao}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDaoSingleton {

    private static volatile IClientDao instance;

    public static IClientDao getInstance() {
        if (instance == null) {
            synchronized (ClientDaoSingleton.class) {
                if (instance == null) {
                    instance = new ClientDao();
                }
            }
        }
        return instance;
    }
}
