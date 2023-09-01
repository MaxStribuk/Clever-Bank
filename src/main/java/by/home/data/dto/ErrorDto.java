package by.home.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * представляет dto класс для хранения неверного входного параметра
 * и комментария об ошибке
 */
@AllArgsConstructor
@Getter
public class ErrorDto implements Serializable {

    private String field;
    private String message;

    @Override
    public String toString() {
        return "field=" + field + ", message=" + message;
    }
}

