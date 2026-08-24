package lebron.task;

/** A task without any date/time attached to it, e.g. "visit new theme park". */
public class Todo extends Task {
    /** Creates a todo task with the given description. */
    public Todo(String description) {
        super(description);
    }

    /** Returns "T", the type icon for todo tasks. */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}
