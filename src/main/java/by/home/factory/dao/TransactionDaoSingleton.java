package by.home.factory.dao;

import by.home.dao.api.ITransactionDao;
import by.home.dao.impl.TransactionDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDaoSingleton {

    private static volatile ITransactionDao instance;

    public static ITransactionDao getInstance() {
        if (instance == null) {
            synchronized (TransactionDaoSingleton.class) {
                if (instance == null) {
                    instance = new TransactionDao();
                }
            }
        }
        return instance;
    }
}
