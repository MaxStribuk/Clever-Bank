package by.home.service.util.validator;

import by.home.service.util.api.IValidation;
import lombok.RequiredArgsConstructor;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * используется для инициализации объекта для валидации объектов dto классов
 */
@RequiredArgsConstructor
public class ValidationManager implements IValidation {

    private final ValidatorFactory validatorFactory;

    @Override
    public Validator open() {
        return validatorFactory.getValidator();
    }

    @Override
    public void close() {
        validatorFactory.close();
    }
}
