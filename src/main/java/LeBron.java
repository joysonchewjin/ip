import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeBron {
    private static final String NAME = "LeBron";

    /** Horizontal divider printed around each block of chatbot output. */
    private static final String LINE = "____________________________________________________________";

    /** ASCII art banner displayed on startup, spelling "LEBRON" in block letters. */
    private static final String banner =
            """
            #     ##### ####  ####   ###  #   #
            #     #     #   # #   # #   # ##  #
            #     ###   ####  ####  #   # # # #
            #     #     #   # #  #  #   # #  ##
            ##### ##### ####  #   #  ###  #   #
            """;

    /** Store of tasks entered by the user, in entry order. */
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        tasks.addAll(Storage.load());
        greet();
        processCommands();
        exit();
    }

    /**
     * Reads commands line by line until "bye" is entered. "list" prints all stored
     * tasks; "mark <n>"/"unmark <n>" update the done status of task n; "delete <n>"
     * removes task n; "todo <description>" adds a to-do task; "deadline
     * <description> /by <date>" adds a deadline task; "event <description> /from
     * <start> /to <end>" adds an event task; any other input is rejected as an
     * unrecognized command.
     */
    private static void processCommands() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            try {
                if (input.equals("list")) {
                    printList();
                } else if (input.startsWith("mark ")) {
                    markTask(input.substring("mark ".length()), true);
                } else if (input.startsWith("unmark ")) {
                    markTask(input.substring("unmark ".length()), false);
                } else if (input.startsWith("delete ")) {
                    deleteTask(input.substring("delete ".length()));
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    storeTask(parseTodo(input.equals("todo") ? "" : input.substring("todo ".length())));
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    storeTask(
                            parseDeadline(input.equals("deadline") ? "" : input.substring("deadline ".length())));
                } else if (input.equals("event") || input.startsWith("event ")) {
                    storeTask(parseEvent(input.equals("event") ? "" : input.substring("event ".length())));
                } else {
                    throw new LeBronException(
                            "That's out of bounds — I don't recognize that command: " + input);
                }
            } catch (LeBronException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(LINE);
        }
    }

    /**
     * Parses the text after "todo " into a {@link Todo}.
     *
     * @throws LeBronException if the description is missing.
     */
    private static Todo parseTodo(String args) throws LeBronException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new LeBronException("Airball! A todo needs a description: todo <description>");
        }
        return new Todo(description);
    }

    /**
     * Parses the text after "deadline " into a {@link Deadline}, expecting the
     * form "<description> /by <date>".
     *
     * @throws LeBronException if the description or the "/by <date>" part is missing.
     */
    private static Deadline parseDeadline(String args) throws LeBronException {
        int markerIndex = args.indexOf(" /by ");
        if (markerIndex == -1) {
            throw new LeBronException(
                    "Travel called! A deadline needs a /by date: deadline <description> /by <date>");
        }
        String description = args.substring(0, markerIndex).trim();
        String by = args.substring(markerIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new LeBronException(
                    "Airball! A deadline needs a description: deadline <description> /by <date>");
        }
        if (by.isEmpty()) {
            throw new LeBronException(
                    "Travel called! A deadline needs a date after /by: deadline <description> /by <date>");
        }
        return new Deadline(description, TaskDateTime.parseInput(by));
    }

    /**
     * Parses the text after "event " into an {@link Event}, expecting the form
     * "<description> /from <start> /to <end>".
     *
     * @throws LeBronException if the description, "/from", or "/to" part is missing.
     */
    private static Event parseEvent(String args) throws LeBronException {
        int fromIndex = args.indexOf(" /from ");
        if (fromIndex == -1) {
            throw new LeBronException(
                    "Travel called! An event needs a /from time: event <description> /from <start> /to <end>");
        }
        int toIndex = args.indexOf(" /to ", fromIndex);
        if (toIndex == -1) {
            throw new LeBronException(
                    "Travel called! An event needs a /to time: event <description> /from <start> /to <end>");
        }
        String description = args.substring(0, fromIndex).trim();
        String from = args.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = args.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new LeBronException(
                    "Airball! An event needs a description: event <description> /from <start> /to <end>");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new LeBronException("Travel called! An event needs both a /from and /to value: "
                    + "event <description> /from <start> /to <end>");
        }
        return new Event(description, from, to);
    }

    /** Stores a newly parsed task, prints an acknowledgement, and persists the updated list to disk. */
    private static void storeTask(Task task) {
        tasks.add(task);
        System.out.println("added: " + task);
        Storage.save(tasks);
    }

    /**
     * Marks or unmarks the task at the given 1-based index (as shown by
     * {@link #printList()}), prints a confirmation, and persists the updated list to disk.
     *
     * @throws LeBronException if the index is not a number or is out of range.
     */
    private static void markTask(String indexText, boolean done) throws LeBronException {
        int index;
        try {
            index = Integer.parseInt(indexText) - 1;
        } catch (NumberFormatException e) {
            throw new LeBronException("That's not on the scoreboard — I don't recognize '"
                    + indexText + "' as a task number: mark <task number>");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new LeBronException("No such play on the roster — there's no task number "
                    + (index + 1) + ". You have " + tasks.size() + " task(s).");
        }
        Task task = tasks.get(index);
        if (done) {
            task.mark();
            System.out.println("Nice! I've marked this task as done:");
        } else {
            task.unmark();
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
        Storage.save(tasks);
    }

    /**
     * Deletes the task at the given 1-based index (as shown by {@link #printList()}),
     * prints a confirmation, and persists the updated list to disk.
     *
     * @throws LeBronException if the index is not a number or is out of range.
     */
    private static void deleteTask(String indexText) throws LeBronException {
        int index;
        try {
            index = Integer.parseInt(indexText) - 1;
        } catch (NumberFormatException e) {
            throw new LeBronException("That's not on the scoreboard — I don't recognize '"
                    + indexText + "' as a task number: delete <task number>");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new LeBronException("No such play on the roster — there's no task number "
                    + (index + 1) + ". You have " + tasks.size() + " task(s).");
        }
        Task task = tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        Storage.save(tasks);
    }

    /** Prints all stored tasks as a numbered list, including their done status. */
    private static void printList() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Prints the startup banner and greeting, wrapped in divider lines. */
    private static void greet() {
        System.out.println(LINE);
        System.out.print(banner);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /** Prints the farewell message, wrapped in a divider line. */
    private static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
