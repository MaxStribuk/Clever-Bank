package by.home.factory.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Validator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorSingleton {

    private static volatile Validator instance;

    public static Validator getInstance() {
        if (instance == null) {
            synchronized (ValidatorSingleton.class) {
                if (instance == null) {
                    instance =  ValidationManagerSingleton.getInstance().open();
                }
            }
        }
        return instance;
    }
}
