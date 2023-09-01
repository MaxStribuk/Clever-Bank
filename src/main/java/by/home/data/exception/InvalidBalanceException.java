package by.home.data.exception;

/**
 * генерируется для случаев, когда денег на счете недостаточно для проведения операции
 */
public class InvalidBalanceException extends RuntimeException {

    public InvalidBalanceException(String message) {
        super(message);
    }
}
