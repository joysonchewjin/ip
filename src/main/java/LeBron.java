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
     * tasks; "mark <n>"/"unmark <n>" update the done status of task n; anything
     * else is stored as a new task and acknowledged.
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
                markTask(input.substring("mark ".length()), true);
            } else if (input.startsWith("unmark ")) {
                markTask(input.substring("unmark ".length()), false);
            } else {
                addItem(input);
            }
            System.out.println(LINE);
        }
    }

    /** Stores an item as a new task and prints an acknowledgement. */
    private static void addItem(String item) {
        tasks[itemCount] = new Task(item);
        itemCount++;
        System.out.println("added: " + item);
    }

    /**
     * Marks or unmarks the task at the given 1-based index (as shown by
     * {@link #printList()}) and prints a confirmation. Prints a friendly error
     * instead of crashing if the index is not a number or is out of range.
     */
    private static void markTask(String indexText, boolean done) {
        int index;
        try {
            index = Integer.parseInt(indexText) - 1;
        } catch (NumberFormatException e) {
            System.out.println("That doesn't look like a task number: " + indexText);
            return;
        }
        if (index < 0 || index >= itemCount) {
            System.out.println("There's no task number " + (index + 1) + ". "
                    + "You have " + itemCount + " task(s).");
            return;
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
