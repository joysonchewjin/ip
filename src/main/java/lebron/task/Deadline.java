package lebron.task;

/** A task that needs to be done before a specific date/time, e.g. "submit report by 11/10/2019 5pm". */
public class Deadline extends Task {
    private final TaskDateTime by;

    /** Creates a deadline task with the given description, due by {@code by}. */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    /** Returns "D", the type icon for deadline tasks. */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /** Returns the date/time this task is due by. */
    public TaskDateTime getBy() {
        return by;
    }

    /**
     * Returns the console display line for this task, including its due date,
     * e.g. "[D][ ] return book (by: Jun 06 2019, 6:00pm)".
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
