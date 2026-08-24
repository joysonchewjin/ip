package lebron.exception;

/**
 * Signals a problem that LeBron can't act on (e.g. a malformed command, missing
 * task, or malformed line in the save file). Messages about user input are
 * written in LeBron's voice; the message is always safe to print or log as a
 * warning.
 */
public class LeBronException extends Exception {
    /** Creates a LeBronException with the given user-facing message. */
    public LeBronException(String message) {
        super(message);
    }
}
