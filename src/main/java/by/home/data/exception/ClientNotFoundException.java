package by.home.data.exception;

/**
 * генерируется, когда искомый клиент не был найден в БД
 */
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
