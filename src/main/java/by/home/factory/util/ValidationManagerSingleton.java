package by.home.factory.util;

import by.home.service.util.api.IValidation;
import by.home.service.util.validator.ValidationManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.bval.jsr.ApacheValidationProvider;

import javax.validation.Validation;

/**
 * создает единственный объект класса, реализующего {@link IValidation}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationManagerSingleton {

    private static volatile IValidation instance;

    public static IValidation getInstance() {
        if (instance == null) {
            synchronized (ValidationManagerSingleton.class) {
                if (instance == null) {
                    instance = new ValidationManager(
                            Validation.byProvider(ApacheValidationProvider.class)
                                    .configure()
                                    .buildValidatorFactory()
                    );
                }
            }
        }
        return instance;
    }
}
