import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves the task list to a plain-text file at {@code ./data/lebron.txt},
 * relative to the working directory. Uses a simple pipe-delimited line format
 * (not JSON, since this project has no third-party dependencies):
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | 2019-06-06T18:00
 * E | 0 | project meeting | 2019-08-06T14:00 | 2019-08-06T16:00
 * </pre>
 * A missing file or missing {@code data/} folder is treated as an empty task
 * list rather than an error; the folder is created on first save.
 */
public class Storage {
    private static final String DELIMITER = " | ";

    private final Path filePath;

    /** Creates a Storage backed by the default save file, {@code ./data/lebron.txt}. */
    public Storage() {
        this(Path.of("data", "lebron.txt"));
    }

    /** Creates a Storage backed by the given file path. */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from disk. Returns an empty list if the file or its containing
     * folder doesn't exist yet, or if the file can't be read. Lines that can't be
     * parsed are skipped with a warning rather than aborting the whole load, so
     * one corrupted line doesn't cost the user their entire task list.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(parseTask(line));
                } catch (LeBronException e) {
                    System.out.println("Skipping unreadable line in save file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read save file, starting with an empty list: " + e.getMessage());
            return new ArrayList<>();
        }
        return tasks;
    }

    /**
     * Overwrites the save file with a full snapshot of the given task list,
     * creating the {@code data/} folder first if it doesn't exist yet. Failures
     * are reported but not thrown, since losing persistence shouldn't crash an
     * otherwise-working interactive session.
     */
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(taskToLine(task));
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Couldn't save the task list: " + e.getMessage());
        }
    }

    /** Serializes one task to a single pipe-delimited line. */
    private static String taskToLine(Task task) {
        String done = task.isDone() ? "1" : "0";
        String base = task.getTypeIcon() + DELIMITER + done + DELIMITER + task.getDescription();
        if (task instanceof Deadline deadline) {
            return base + DELIMITER + deadline.getBy().toStorageString();
        } else if (task instanceof Event event) {
            return base + DELIMITER + event.getFrom().toStorageString()
                    + DELIMITER + event.getTo().toStorageString();
        }
        return base;
    }

    /**
     * Parses one pipe-delimited line back into a {@link Task}.
     *
     * @throws LeBronException if the line is malformed (wrong field count, unknown
     *     type icon, or a non-"0"/"1" done flag).
     */
    private static Task parseTask(String line) throws LeBronException {
        String[] fields = line.split("\\s*\\|\\s*");
        if (fields.length < 3) {
            throw new LeBronException("Malformed save line: " + line);
        }
        String type = fields[0];
        boolean done = parseDoneFlag(fields[1], line);
        String description = fields[2];

        Task task;
        switch (type) {
            case "T" -> task = new Todo(description);
            case "D" -> {
                if (fields.length < 4) {
                    throw new LeBronException("Malformed deadline save line: " + line);
                }
                task = new Deadline(description, TaskDateTime.parseStorage(fields[3]));
            }
            case "E" -> {
                if (fields.length < 5) {
                    throw new LeBronException("Malformed event save line: " + line);
                }
                task = new Event(description, TaskDateTime.parseStorage(fields[3]),
                        TaskDateTime.parseStorage(fields[4]));
            }
            default -> throw new LeBronException("Unknown task type in save line: " + line);
        }
        if (done) {
            task.mark();
        }
        return task;
    }

    private static boolean parseDoneFlag(String field, String line) throws LeBronException {
        if (field.equals("1")) {
            return true;
        } else if (field.equals("0")) {
            return false;
        }
        throw new LeBronException("Malformed done-flag in save line: " + line);
    }
}
