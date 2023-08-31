package by.home.factory.service;

import by.home.factory.util.ValidatorSingleton;
import by.home.service.api.IStatementService;
import by.home.service.impl.StatementService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementServiceSingleton {

    private static volatile IStatementService instance;

    public static IStatementService getInstance() {
        if (instance == null) {
            synchronized (StatementServiceSingleton.class) {
                if (instance == null) {
                    instance = new StatementService(
                            AccountServiceSingleton.getInstance(),
                            TransactionServiceSingleton.getInstance(),
                            ValidatorSingleton.getInstance(),
                            PdfServiceSingleton.getInstance(),
                            BankServiceSingleton.getInstance(),
                            ClientServiceSingleton.getInstance()
                    );
                }
            }
        }
        return instance;
    }
}
