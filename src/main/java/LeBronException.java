/**
 * Signals a problem with user input that LeBron can't act on (e.g. a malformed
 * command or missing task). The message is written in LeBron's voice and is safe
 * to print directly to the user.
 */
public class LeBronException extends Exception {
    public LeBronException(String message) {
        super(message);
    }
}
