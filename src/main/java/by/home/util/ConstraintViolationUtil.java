package by.home.util;

import by.home.data.dto.ErrorDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * класс для обработки ошибок входных параметров
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintViolationUtil {

    public static List<ErrorDto> getErrors(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(ConstraintViolationUtil::createErrorDto)
                .toList();
    }

    private static ErrorDto createErrorDto(ConstraintViolation<?> field) {
        Iterator<Path.Node> iterator = field.getPropertyPath()
                .iterator();
        Path.Node next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
        }
        String name = Objects.requireNonNull(next)
                .getName();
        return new ErrorDto(name, field.getMessage());
    }
}
