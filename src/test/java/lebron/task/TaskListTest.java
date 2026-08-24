package lebron.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import lebron.exception.LeBronException;

public class TaskListTest {

    @Test
    public void constructor_noArgs_startsEmpty() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.size());
    }

    @Test
    public void add_appendsTaskAndGrowsSize() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write code"));

        assertEquals(2, tasks.size());
    }

    @Test
    public void get_validIndex_returnsTaskInEntryOrder() throws LeBronException {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read book");
        Todo second = new Todo("write code");
        tasks.add(first);
        tasks.add(second);

        assertEquals(first, tasks.get(0));
        assertEquals(second, tasks.get(1));
    }

    @Test
    public void get_negativeIndex_throwsLeBronException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(LeBronException.class, () -> tasks.get(-1));
    }

    @Test
    public void get_indexEqualToSize_throwsLeBronException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(LeBronException.class, () -> tasks.get(1));
    }

    @Test
    public void get_emptyList_throwsLeBronException() {
        TaskList tasks = new TaskList();

        assertThrows(LeBronException.class, () -> tasks.get(0));
    }

    @Test
    public void mark_validIndex_marksTaskDoneAndReturnsIt() throws LeBronException {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");
        tasks.add(todo);

        Task marked = tasks.mark(0);

        assertTrue(marked.isDone());
        assertEquals(todo, marked);
    }

    @Test
    public void mark_indexOutOfRange_throwsLeBronException() {
        TaskList tasks = new TaskList();

        assertThrows(LeBronException.class, () -> tasks.mark(0));
    }

    @Test
    public void unmark_validIndex_marksTaskNotDoneAndReturnsIt() throws LeBronException {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");
        todo.mark();
        tasks.add(todo);

        Task unmarked = tasks.unmark(0);

        assertFalse(unmarked.isDone());
    }

    @Test
    public void unmark_indexOutOfRange_throwsLeBronException() {
        TaskList tasks = new TaskList();

        assertThrows(LeBronException.class, () -> tasks.unmark(0));
    }

    @Test
    public void delete_validIndex_removesTaskAndShiftsLaterIndices() throws LeBronException {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read book");
        Todo second = new Todo("write code");
        tasks.add(first);
        tasks.add(second);

        Task deleted = tasks.delete(0);

        assertEquals(first, deleted);
        assertEquals(1, tasks.size());
        assertEquals(second, tasks.get(0));
    }

    @Test
    public void delete_indexOutOfRange_throwsLeBronExceptionAndLeavesListUnmodified() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(LeBronException.class, () -> tasks.delete(5));
        assertEquals(1, tasks.size());
    }

    @Test
    public void constructor_withInitialList_copiesDefensively() {
        List<Task> initial = new ArrayList<>();
        initial.add(new Todo("read book"));

        TaskList tasks = new TaskList(initial);
        initial.add(new Todo("write code"));

        assertEquals(1, tasks.size());
    }

    @Test
    public void getAll_returnsTasksInEntryOrder() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read book");
        Todo second = new Todo("write code");
        tasks.add(first);
        tasks.add(second);

        List<Task> all = tasks.getAll();

        assertEquals(List.of(first, second), all);
    }

    @Test
    public void getAll_isUnmodifiable() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        List<Task> all = tasks.getAll();

        assertThrows(UnsupportedOperationException.class, () -> all.add(new Todo("write code")));
    }

    @Test
    public void find_matchingKeyword_returnsMatchesInEntryOrder() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read book");
        Todo second = new Todo("write code");
        Todo third = new Todo("return book");
        tasks.add(first);
        tasks.add(second);
        tasks.add(third);

        List<Task> matches = tasks.find("book");

        assertEquals(List.of(first, third), matches);
    }

    @Test
    public void find_differentCase_matchesCaseInsensitively() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");
        tasks.add(todo);

        List<Task> matches = tasks.find("BOOK");

        assertEquals(List.of(todo), matches);
    }

    @Test
    public void find_noMatch_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        List<Task> matches = tasks.find("homework");

        assertEquals(List.of(), matches);
    }
}
