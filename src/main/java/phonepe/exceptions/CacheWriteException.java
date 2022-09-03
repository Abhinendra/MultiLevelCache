package phonepe.exception;

public class CacheWriteException extends RuntimeException {
    public CacheWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
