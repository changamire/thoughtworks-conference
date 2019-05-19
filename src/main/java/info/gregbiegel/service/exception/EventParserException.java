package info.gregbiegel.service.exception;

/**
 * A class representing an exception parsing an input event
 *
 */
public class EventParserException extends RuntimeException {

    public EventParserException(final String message) {
        super(message);
    }
    
    public EventParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
