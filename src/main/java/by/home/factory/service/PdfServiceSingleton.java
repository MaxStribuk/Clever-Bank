package by.home.factory.service;

import by.home.service.api.IPdfService;
import by.home.service.impl.PdfService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PdfServiceSingleton {

    private static volatile IPdfService instance;

    public static IPdfService getInstance() {
        if (instance == null) {
            synchronized (PdfServiceSingleton.class) {
                if (instance == null) {
                    instance = new PdfService();
                }
            }
        }
        return instance;
    }
}
