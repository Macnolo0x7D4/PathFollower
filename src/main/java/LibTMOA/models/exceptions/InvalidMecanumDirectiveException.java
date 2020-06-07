package LibTMOA.models.exceptions;

/**
 * Exception for Invalid Mecanum Directives
 */
public class InvalidMecanumDirectiveException extends Exception{

    /**
     * Creates an InvalidMecanumDirectiveException.
     * @param message Message to print in Exception Stack Trace
     */
    public InvalidMecanumDirectiveException(String message) {
        super(message);
    }
}
