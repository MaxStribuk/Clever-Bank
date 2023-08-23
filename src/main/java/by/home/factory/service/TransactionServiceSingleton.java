package by.home.factory.service;

import by.home.factory.dao.TransactionDaoSingleton;
import by.home.service.api.ITransactionService;
import by.home.service.impl.TransactionService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionServiceSingleton {

    private static volatile ITransactionService instance;

    public static ITransactionService getInstance() {
        if (instance == null) {
            synchronized (TransactionServiceSingleton.class) {
                if (instance == null) {
                    instance = new TransactionService(
                            TransactionDaoSingleton.getInstance());
                }
            }
        }
        return instance;
    }
}
