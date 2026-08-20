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

    /** Maximum number of stored items; the spec guarantees no more than 100 will be entered. */
    private static final int MAX_ITEMS = 100;

    /** Fixed-size store of tasks entered by the user, in entry order. */
    private static final Task[] tasks = new Task[MAX_ITEMS];

    /** Number of tasks currently stored in {@link #tasks}. */
    private static int itemCount = 0;

    public static void main(String[] args) {
        greet();
        processCommands();
        exit();
    }

    /**
     * Reads commands line by line until "bye" is entered. "list" prints all stored
     * tasks; "mark <n>"/"unmark <n>" update the done status of task n; "todo
     * <description>" adds a to-do task; "deadline <description> /by <date>"
     * adds a deadline task; "event <description> /from <start> /to <end>" adds
     * an event task; any other input is rejected as an unrecognized command.
     */
    private static void processCommands() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                try {
                    markTask(input.substring("mark ".length()), true);
                } catch (LeBronException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    markTask(input.substring("unmark ".length()), false);
                } catch (LeBronException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("todo") || input.startsWith("todo ")) {
                try {
                    storeTask(parseTodo(input.equals("todo") ? "" : input.substring("todo ".length())));
                } catch (LeBronException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                try {
                    storeTask(parseDeadline(input.equals("deadline") ? "" : input.substring("deadline ".length())));
                } catch (LeBronException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("event") || input.startsWith("event ")) {
                try {
                    storeTask(parseEvent(input.equals("event") ? "" : input.substring("event ".length())));
                } catch (LeBronException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("I don't recognize that command: " + input);
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
        return new Deadline(description, by);
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

    /**
     * Stores a newly parsed task and prints an acknowledgement, unless parsing
     * already failed (indicated by {@code task} being {@code null}), in which
     * case the caller has already printed an error and nothing further happens.
     */
    private static void storeTask(Task task) {
        if (task == null) {
            return;
        }
        tasks[itemCount] = task;
        itemCount++;
        System.out.println("added: " + task);
    }

    /**
     * Marks or unmarks the task at the given 1-based index (as shown by
     * {@link #printList()}) and prints a confirmation.
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
        if (index < 0 || index >= itemCount) {
            throw new LeBronException("No such play on the roster — there's no task number "
                    + (index + 1) + ". You have " + itemCount + " task(s).");
        }
        Task task = tasks[index];
        if (done) {
            task.mark();
            System.out.println("Nice! I've marked this task as done:");
        } else {
            task.unmark();
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /** Prints all stored tasks as a numbered list, including their done status. */
    private static void printList() {
        for (int i = 0; i < itemCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
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
