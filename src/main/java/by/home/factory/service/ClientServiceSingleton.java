package by.home.factory.service;

import by.home.factory.dao.ClientDaoSingleton;
import by.home.service.api.IClientService;
import by.home.service.impl.ClientService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientServiceSingleton {

    private static volatile IClientService instance;

    public static IClientService getInstance() {
        if (instance == null) {
            synchronized (ClientServiceSingleton.class) {
                if (instance == null) {
                    instance = new ClientService(
                            ClientDaoSingleton.getInstance()
                    );
                }
            }
        }
        return instance;
    }
}
