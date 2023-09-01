package by.home.data.exception;

/**
 * генерируется, когда искомый аккаунт не был найден в БД
 */
public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(String message) {
        super(message);
    }
}
