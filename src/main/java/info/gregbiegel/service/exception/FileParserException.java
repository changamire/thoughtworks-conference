package info.gregbiegel.service.exception;

/**
 * A class representing an exception parsing the input file
 *
 */
public class FileParserException extends Exception {

    public FileParserException(final String message) {
        super(message);
    }
    
    public FileParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
