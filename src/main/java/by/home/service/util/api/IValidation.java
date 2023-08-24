package by.home.service.util.api;

import javax.validation.Validator;

public interface IValidation {

    Validator open();

    void close() throws Exception;
}
