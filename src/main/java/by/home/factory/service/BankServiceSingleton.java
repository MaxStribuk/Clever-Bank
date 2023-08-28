package by.home.factory.service;

import by.home.factory.dao.BankDaoSingleton;
import by.home.factory.util.ModelMapperSingleton;
import by.home.service.api.IBankService;
import by.home.service.impl.BankService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankServiceSingleton {

    private static volatile IBankService instance;

    public static IBankService getInstance() {
        if (instance == null) {
            synchronized (BankServiceSingleton.class) {
                if (instance == null) {
                    instance = new BankService(
                            BankDaoSingleton.getInstance(),
                            ModelMapperSingleton.getInstance()
                    );
                }
            }
        }
        return instance;
    }
}
