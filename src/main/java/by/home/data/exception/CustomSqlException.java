package by.home.data.exception;

public class CustomSqlException extends RuntimeException {

    public CustomSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomSqlException(Throwable cause) {
        super(cause);
    }
}
