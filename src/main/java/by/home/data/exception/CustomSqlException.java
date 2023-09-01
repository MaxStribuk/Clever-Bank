package by.home.data.exception;

/**
 * генерируется при любых ошибках при работе с БД.
 * Оборачивает собой проверяемое исключение {@link java.sql.SQLException}
 */
public class CustomSqlException extends RuntimeException {

    public CustomSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomSqlException(Throwable cause) {
        super(cause);
    }
}
