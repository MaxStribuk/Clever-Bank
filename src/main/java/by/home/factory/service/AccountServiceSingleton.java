package by.home.factory.service;

import by.home.factory.dao.AccountDaoSingleton;
import by.home.factory.util.ModelMapperSingleton;
import by.home.factory.util.ValidatorSingleton;
import by.home.service.api.IAccountService;
import by.home.service.impl.AccountService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * создает единственный объект класса, реализующего {@link IAccountService}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountServiceSingleton {

    private static volatile IAccountService instance;

    public static IAccountService getInstance() {
        if (instance == null) {
            synchronized (AccountServiceSingleton.class) {
                if (instance == null) {
                    instance = new AccountService(
                            AccountDaoSingleton.getInstance(),
                            ValidatorSingleton.getInstance(),
                            TransactionServiceSingleton.getInstance(),
                            CheckServiceSingleton.getInstance(),
                            ModelMapperSingleton.getInstance(),
                            BankServiceSingleton.getInstance()
                    );
                }
            }
        }
        return instance;
    }
}
