package lebron.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void constructor_setsDescriptionAndNotDone() {
        Todo todo = new Todo("read book");

        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    public void mark_marksTaskAsDone() {
        Todo todo = new Todo("read book");

        todo.mark();

        assertTrue(todo.isDone());
    }

    @Test
    public void unmark_marksTaskAsNotDone() {
        Todo todo = new Todo("read book");
        todo.mark();

        todo.unmark();

        assertFalse(todo.isDone());
    }

    @Test
    public void getStatusIcon_notDone_returnsBlank() {
        Todo todo = new Todo("read book");

        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void getStatusIcon_done_returnsX() {
        Todo todo = new Todo("read book");
        todo.mark();

        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void getTypeIcon_returnsT() {
        Todo todo = new Todo("read book");

        assertEquals("T", todo.getTypeIcon());
    }

    @Test
    public void toString_notDone_formatsWithBlankStatus() {
        Todo todo = new Todo("read book");

        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toString_done_formatsWithXStatus() {
        Todo todo = new Todo("read book");
        todo.mark();

        assertEquals("[T][X] read book", todo.toString());
    }
}
