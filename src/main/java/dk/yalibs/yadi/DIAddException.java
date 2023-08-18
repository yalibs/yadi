package dk.yalibs.yadi;

/**
 * Exception that happens during addition to {@link DI}.
 */
public class DIAddException extends RuntimeException {
    /**
     * Default exception constructor
     * @param message a descriptive message of the error
     */
    public DIAddException(String message) {
        super(message);
    }
}

