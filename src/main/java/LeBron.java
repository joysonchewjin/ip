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
     * items; anything else is stored as a new item and acknowledged.
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
