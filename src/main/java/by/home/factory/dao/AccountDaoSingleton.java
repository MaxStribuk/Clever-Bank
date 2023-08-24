package by.home.factory.dao;

import by.home.dao.api.IAccountDao;
import by.home.dao.impl.AccountDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDaoSingleton {

    private static volatile IAccountDao instance;

    public static IAccountDao getInstance() {
        if (instance == null) {
            synchronized (AccountDaoSingleton.class) {
                if (instance == null) {
                    instance = new AccountDao();
                }
            }
        }
        return instance;
    }
}
