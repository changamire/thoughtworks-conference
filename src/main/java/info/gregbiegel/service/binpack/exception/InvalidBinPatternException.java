package info.gregbiegel.service.binpack.exception;

/**
 * A class representing an invalid bin pattern passed to the bin packing algorithm
 *
 */
public class InvalidBinPatternException extends RuntimeException {
    
    public InvalidBinPatternException() {
        super("Invalid bin pattern provided");
    }
}
