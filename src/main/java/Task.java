/**
 * A single task entered by the user, tracked with a description and a
 * done/not-done status. Concrete subclasses ({@link Todo}, {@link Deadline},
 * {@link Event}) add their own type icon and any type-specific details.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as done. */
    public void mark() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void unmark() {
        isDone = false;
    }

    /** Returns "X" if this task is done, or a blank space otherwise. */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the single-letter icon identifying this task's type, e.g. "T", "D", "E". */
    public abstract String getTypeIcon();

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
