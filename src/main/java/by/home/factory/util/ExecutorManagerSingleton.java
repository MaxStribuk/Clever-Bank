package by.home.factory.util;

import by.home.service.util.validator.ExecutorManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorManagerSingleton {

    private static volatile ExecutorManager instance;

    public static ExecutorManager getInstance() {
        if (instance == null) {
            synchronized (ExecutorManagerSingleton.class) {
                if (instance == null) {
                    instance = new ExecutorManager();
                }
            }
        }
        return instance;
    }
}
