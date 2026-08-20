/** A task without any date/time attached to it, e.g. "visit new theme park". */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
