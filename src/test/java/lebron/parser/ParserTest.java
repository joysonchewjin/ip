package lebron.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import lebron.exception.LeBronException;
import lebron.storage.Storage;
import lebron.task.Deadline;
import lebron.task.Event;
import lebron.task.Task;
import lebron.task.TaskList;
import lebron.task.Todo;
import lebron.ui.Ui;

public class ParserTest {

    @TempDir
    Path tempDir;

    private final PrintStream originalOut = System.out;

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(tempDir.resolve("tasks.txt"));
    }

    @AfterEach
    public void restoreOut() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_list_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.execute("list", tasks, ui, storage));
    }

    @Test
    public void execute_todo_addsTodoToList() throws LeBronException {
        Parser.execute("todo read book", tasks, ui, storage);

        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertInstanceOf(Todo.class, task);
        assertEquals("read book", task.getDescription());
    }

    @Test
    public void execute_todoEmptyDescription_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("todo", tasks, ui, storage));
    }

    @Test
    public void execute_todoBlankDescription_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("todo   ", tasks, ui, storage));
    }

    @Test
    public void execute_deadline_addsDeadlineToList() throws LeBronException {
        Parser.execute("deadline return book /by 2/12/2019 1800", tasks, ui, storage);

        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00pm)", task.toString());
    }

    @Test
    public void execute_deadlineMissingBy_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("deadline return book", tasks, ui, storage));
    }

    @Test
    public void execute_deadlineEmptyDescription_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("deadline /by 2/12/2019", tasks, ui, storage));
    }

    @Test
    public void execute_deadlineEmptyDate_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("deadline return book /by ", tasks, ui, storage));
    }

    @Test
    public void execute_deadlineInvalidDateText_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("deadline return book /by nonsense", tasks, ui, storage));
    }

    @Test
    public void execute_event_addsEventToList() throws LeBronException {
        Parser.execute("event project meeting /from 6/8/2019 1400 /to 6/8/2019 1600", tasks, ui, storage);

        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertInstanceOf(Event.class, task);
        assertEquals("[E][ ] project meeting (from: Aug 06 2019, 2:00pm to: Aug 06 2019, 4:00pm)",
                task.toString());
    }

    @Test
    public void execute_eventMissingFrom_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("event project meeting /to 6/8/2019", tasks, ui, storage));
    }

    @Test
    public void execute_eventMissingTo_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("event project meeting /from 6/8/2019", tasks, ui, storage));
    }

    @Test
    public void execute_eventEmptyFromAndTo_throwsLeBronException() {
        assertThrows(LeBronException.class,
                () -> Parser.execute("event project meeting /from  /to ", tasks, ui, storage));
    }

    @Test
    public void execute_mark_marksTaskDone() throws LeBronException {
        Parser.execute("todo read book", tasks, ui, storage);

        Parser.execute("mark 1", tasks, ui, storage);

        assertTrue(tasks.get(0).isDone());
    }

    @Test
    public void execute_markNonNumericIndex_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("mark abc", tasks, ui, storage));
    }

    @Test
    public void execute_markOutOfRangeIndex_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("mark 99", tasks, ui, storage));
    }

    @Test
    public void execute_unmark_marksTaskNotDone() throws LeBronException {
        Parser.execute("todo read book", tasks, ui, storage);
        Parser.execute("mark 1", tasks, ui, storage);

        Parser.execute("unmark 1", tasks, ui, storage);

        assertEquals(false, tasks.get(0).isDone());
    }

    @Test
    public void execute_delete_removesTask() throws LeBronException {
        Parser.execute("todo read book", tasks, ui, storage);

        Parser.execute("delete 1", tasks, ui, storage);

        assertEquals(0, tasks.size());
    }

    @Test
    public void execute_deleteOutOfRangeIndex_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("delete 1", tasks, ui, storage));
    }

    @Test
    public void execute_unrecognizedCommand_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> Parser.execute("frobnicate", tasks, ui, storage));
    }

    @Test
    public void execute_todo_persistsToStorage() throws LeBronException {
        Parser.execute("todo read book", tasks, ui, storage);

        Storage reloaded = new Storage(tempDir.resolve("tasks.txt"));
        assertEquals(1, reloaded.load().size());
    }
}
