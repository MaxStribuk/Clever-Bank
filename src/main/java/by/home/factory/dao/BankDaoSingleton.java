package by.home.factory.dao;

import by.home.dao.api.IBankDao;
import by.home.dao.impl.BankDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankDaoSingleton {

    private static volatile IBankDao instance;

    public static IBankDao getInstance() {
        if (instance == null) {
            synchronized (BankDaoSingleton.class) {
                if (instance == null) {
                    instance = new BankDao();
                }
            }
        }
        return instance;
    }
}
