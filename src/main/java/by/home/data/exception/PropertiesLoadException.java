package by.home.data.exception;

/**
 * генерируется, если не удалось прочить файл properties
 */
public class PropertiesLoadException extends RuntimeException {

    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
