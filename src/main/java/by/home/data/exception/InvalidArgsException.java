package by.home.data.exception;

/**
 * генерируется для случаев неккоректных входных данных
 */
public class InvalidArgsException extends RuntimeException {

    public InvalidArgsException(String message) {
        super(message);
    }
}
