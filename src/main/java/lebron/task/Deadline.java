package lebron.task;

/** A task that needs to be done before a specific date/time, e.g. "submit report by 11/10/2019 5pm". */
public class Deadline extends Task {
    private final TaskDateTime by;

    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /** Returns the date/time this task is due by. */
    public TaskDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
