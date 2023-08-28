package by.home.factory.service;

import by.home.service.api.ICheckService;
import by.home.service.impl.CheckService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckServiceSingleton {

    private static volatile ICheckService instance;

    public static ICheckService getInstance() {
        if (instance == null) {
            synchronized (CheckServiceSingleton.class) {
                if (instance == null) {
                    instance = new CheckService(PdfServiceSingleton.getInstance());
                }
            }
        }
        return instance;
    }
}
