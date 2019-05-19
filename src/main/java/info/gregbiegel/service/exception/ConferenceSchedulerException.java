package info.gregbiegel.service.exception;

/**
 * A class representing an exception scheduling conference events
 *
 */
public class ConferenceSchedulerException extends Exception {

    public ConferenceSchedulerException (String message) {
        super(message);
    }

    public ConferenceSchedulerException (Exception cause) {
        super(cause);
    }
}
