/** A task that starts at a specific date/time and ends at a specific date/time. */
public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /** Returns the raw "from" date/time text, unchanged from what was entered. */
    public String getFrom() {
        return from;
    }

    /** Returns the raw "to" date/time text, unchanged from what was entered. */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
