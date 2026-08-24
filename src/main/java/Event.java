/** A task that starts at a specific date/time and ends at a specific date/time. */
public class Event extends Task {
    private final TaskDateTime from;
    private final TaskDateTime to;

    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /** Returns the date/time this event starts. */
    public TaskDateTime getFrom() {
        return from;
    }

    /** Returns the date/time this event ends. */
    public TaskDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
