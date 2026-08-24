package lebron.task;

/** A task that starts at a specific date/time and ends at a specific date/time. */
public class Event extends Task {
    private final TaskDateTime from;
    private final TaskDateTime to;

    /** Creates an event task with the given description, spanning {@code from} to {@code to}. */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns "E", the type icon for event tasks. */
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

    /**
     * Returns the console display line for this task, including its start and
     * end times, e.g. "[E][ ] project meeting (from: Aug 06 2019, 2:00pm to: Aug 06 2019, 4:00pm)".
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
