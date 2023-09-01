package by.home.data.exception;

/**
 * генерируется, когда искомый банк не был найден в БД
 */
public class BankNotFoundException extends RuntimeException {

    public BankNotFoundException(String message) {
        super(message);
    }
}
