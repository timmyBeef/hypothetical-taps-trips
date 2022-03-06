package exception;


public class CsvReadException extends RuntimeException {
    public CsvReadException(String message) {
        super(message);
    }
}
