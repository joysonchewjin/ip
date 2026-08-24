package lebron.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lebron.task.Task;
import lebron.task.Todo;

public class UiTest {
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    public void redirectOut() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String output() {
        return capturedOut.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void showWelcome_printsGreeting() {
        new Ui().showWelcome();

        assertTrue(output().contains("Hello! I'm LeBron."));
    }

    @Test
    public void showGoodbye_printsFarewell() {
        new Ui().showGoodbye();

        assertTrue(output().contains("Bye. Hope to see you again soon!"));
    }

    @Test
    public void showLine_printsDivider() {
        new Ui().showLine();

        assertTrue(output().contains("____"));
    }

    @Test
    public void showTaskList_emptyList_printsNoLines() {
        new Ui().showTaskList(List.of());

        assertEquals("", output());
    }

    @Test
    public void showTaskList_multipleTasks_printsOneBasedNumberedLines() {
        Task first = new Todo("read book");
        Task second = new Todo("write code");

        new Ui().showTaskList(List.of(first, second));

        assertEquals("1. " + first + System.lineSeparator() + "2. " + second + System.lineSeparator(),
                output());
    }

    @Test
    public void showAdded_printsAddedConfirmationWithTask() {
        Task task = new Todo("read book");

        new Ui().showAdded(task);

        assertTrue(output().contains("added: " + task));
    }

    @Test
    public void showMarked_printsMarkedConfirmationWithTask() {
        Task task = new Todo("read book");

        new Ui().showMarked(task);

        assertTrue(output().contains("marked this task as done"));
        assertTrue(output().contains(task.toString()));
    }

    @Test
    public void showUnmarked_printsUnmarkedConfirmationWithTask() {
        Task task = new Todo("read book");

        new Ui().showUnmarked(task);

        assertTrue(output().contains("marked this task as not done yet"));
        assertTrue(output().contains(task.toString()));
    }

    @Test
    public void showDeleted_printsDeletedConfirmationWithTaskAndRemainingCount() {
        Task task = new Todo("read book");

        new Ui().showDeleted(task, 2);

        assertTrue(output().contains("removed this task"));
        assertTrue(output().contains(task.toString()));
        assertTrue(output().contains("Now you have 2 tasks in the list."));
    }

    @Test
    public void showError_printsMessage() {
        new Ui().showError("something went wrong");

        assertTrue(output().contains("something went wrong"));
    }

    @Test
    public void readCommand_multipleLines_returnsEachLineInOrder() {
        System.setIn(new ByteArrayInputStream("todo read book\nbye\n".getBytes(StandardCharsets.UTF_8)));
        Ui ui = new Ui();

        assertEquals("todo read book", ui.readCommand());
        assertEquals("bye", ui.readCommand());
    }

    @Test
    public void readCommand_endOfInput_returnsNull() {
        System.setIn(new ByteArrayInputStream("bye\n".getBytes(StandardCharsets.UTF_8)));
        Ui ui = new Ui();
        ui.readCommand();

        assertNull(ui.readCommand());
    }
}
