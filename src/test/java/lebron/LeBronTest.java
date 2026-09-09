package lebron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import lebron.storage.Storage;

public class LeBronTest {

    @TempDir
    Path tempDir;

    private LeBron newLeBron() {
        return new LeBron(new Storage(tempDir.resolve("tasks.txt")));
    }

    @Test
    public void getResponse_todoCommand_returnsAddedConfirmation() {
        LeBron leBron = newLeBron();

        String response = leBron.getResponse("todo read book");

        assertTrue(response.contains("added: [T][ ] read book"));
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        LeBron leBron = newLeBron();

        String response = leBron.getResponse("dunk the ball");

        assertTrue(response.contains("out of bounds"));
    }

    @Test
    public void getResponse_byeCommand_returnsGoodbyeMessage() {
        LeBron leBron = newLeBron();

        String response = leBron.getResponse("bye");

        assertTrue(response.contains("Bye. Hope to see you again soon!"));
    }

    @Test
    public void getResponse_sequentialCommands_shareStateAcrossCalls() {
        LeBron leBron = newLeBron();

        leBron.getResponse("todo read book");
        String response = leBron.getResponse("list");

        assertEquals("1. [T][ ] read book", response);
    }

    @Test
    public void isExit_byeInput_returnsTrue() {
        LeBron leBron = newLeBron();

        assertTrue(leBron.isExit("bye"));
    }

    @Test
    public void isExit_otherInput_returnsFalse() {
        LeBron leBron = newLeBron();

        assertFalse(leBron.isExit("todo read book"));
    }
}
