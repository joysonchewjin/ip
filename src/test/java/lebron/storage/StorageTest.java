package lebron.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import lebron.task.Deadline;
import lebron.task.Event;
import lebron.task.Task;
import lebron.task.TaskDateTime;
import lebron.task.Todo;

public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage newStorage() {
        return new Storage(tempDir.resolve("tasks.txt"));
    }

    @Test
    public void load_missingFile_returnsEmptyList() {
        Storage storage = newStorage();

        List<Task> tasks = storage.load();

        assertEquals(0, tasks.size());
    }

    @Test
    public void saveThenLoad_roundTripsTodoDeadlineEventWithDoneFlags() throws Exception {
        Storage storage = newStorage();
        Todo todo = new Todo("read book");
        todo.mark();
        Deadline deadline = new Deadline("return book", TaskDateTime.parseInput("2/12/2019 1800"));
        Event event = new Event("project meeting",
                TaskDateTime.parseInput("6/8/2019"), TaskDateTime.parseInput("7/8/2019"));
        event.mark();

        storage.save(List.of(todo, deadline, event));
        List<Task> loaded = storage.load();

        assertEquals(3, loaded.size());
        assertEquals("[T][X] read book", loaded.get(0).toString());
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00pm)", loaded.get(1).toString());
        assertEquals("[E][X] project meeting (from: Aug 06 2019 to: Aug 07 2019)", loaded.get(2).toString());
    }

    @Test
    public void save_missingParentDirectories_createsThem() throws IOException {
        Storage storage = new Storage(tempDir.resolve("nested").resolve("dir").resolve("tasks.txt"));

        storage.save(List.of(new Todo("read book")));

        assertTrue(Files.exists(tempDir.resolve("nested").resolve("dir").resolve("tasks.txt")));
    }

    @Test
    public void load_lineWithUnknownTypeIcon_isSkipped() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T | 0 | read book\nX | 0 | mystery task\n");
        Storage storage = new Storage(file);

        List<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals("[T][ ] read book", loaded.get(0).toString());
    }

    @Test
    public void load_lineWithBadDoneFlag_isSkipped() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T | 0 | read book\nT | maybe | write code\n");
        Storage storage = new Storage(file);

        List<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals("[T][ ] read book", loaded.get(0).toString());
    }

    @Test
    public void load_deadlineLineMissingDateField_isSkipped() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T | 0 | read book\nD | 0 | return book\n");
        Storage storage = new Storage(file);

        List<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals("[T][ ] read book", loaded.get(0).toString());
    }
}
