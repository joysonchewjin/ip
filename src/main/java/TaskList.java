import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the tasks entered by the user, in entry order, and the operations
 * that mutate them. Pure in-memory data structure: it does no printing and
 * no file I/O, so it can be reused regardless of how tasks are displayed or
 * persisted.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list pre-populated with {@code initial}, e.g. from {@link Storage#load()}. */
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /** Appends a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given 0-based index.
     *
     * @throws LeBronException if the index is out of range.
     */
    public Task get(int index) throws LeBronException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Marks the task at the given 0-based index as done and returns it.
     *
     * @throws LeBronException if the index is out of range.
     */
    public Task mark(int index) throws LeBronException {
        checkIndex(index);
        Task task = tasks.get(index);
        task.mark();
        return task;
    }

    /**
     * Marks the task at the given 0-based index as not done and returns it.
     *
     * @throws LeBronException if the index is out of range.
     */
    public Task unmark(int index) throws LeBronException {
        checkIndex(index);
        Task task = tasks.get(index);
        task.unmark();
        return task;
    }

    /**
     * Removes and returns the task at the given 0-based index.
     *
     * @throws LeBronException if the index is out of range.
     */
    public Task delete(int index) throws LeBronException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /** Returns the number of tasks currently stored. */
    public int size() {
        return tasks.size();
    }

    /** Returns a read-only view of all tasks, in entry order. */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    /** @throws LeBronException if {@code index} is not a valid 0-based index into the list. */
    private void checkIndex(int index) throws LeBronException {
        if (index < 0 || index >= tasks.size()) {
            throw new LeBronException("No such play on the roster — there's no task number "
                    + (index + 1) + ". You have " + tasks.size() + " task(s).");
        }
    }
}
