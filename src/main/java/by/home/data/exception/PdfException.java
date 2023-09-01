package by.home.data.exception;

/**
 * генерируеься для ошибок, связанных с созданием pdf файла,
 * оборачивает собой проверяемые исключения
 */
public class PdfException extends RuntimeException{

    public PdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
