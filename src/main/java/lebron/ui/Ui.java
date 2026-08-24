package lebron.ui;

import java.util.List;
import java.util.Scanner;

import lebron.task.Task;

/**
 * Handles all interaction with the user: printing the greeting, farewell,
 * task list, and confirmation/error messages, and reading command input
 * from the console.
 */
public class Ui {
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

    private final Scanner scanner = new Scanner(System.in);

    /** Prints the startup banner and greeting, wrapped in divider lines. */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.print(banner);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /** Prints the farewell message, wrapped in a divider line. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Reads and returns the next line of input, or {@code null} if there's
     * no more input (end of stream), so callers can loop with
     * {@code while ((input = ui.readCommand()) != null)}.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Prints the divider line; called once after handling each command. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Prints all tasks in {@code tasks} as a numbered list, including their done status. */
    public void showTaskList(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Prints {@code tasks} as a numbered list under a "matching tasks" header. */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        showTaskList(tasks);
    }

    /** Prints an acknowledgement that {@code task} was added. */
    public void showAdded(Task task) {
        System.out.println("added: " + task);
    }

    /** Prints a confirmation that {@code task} was marked as done. */
    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /** Prints a confirmation that {@code task} was marked as not done. */
    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Prints a confirmation that {@code task} was removed, and how many tasks remain. */
    public void showDeleted(Task task, int remainingCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingCount + " tasks in the list.");
    }

    /** Prints an error message, e.g. from a caught {@link LeBronException}. */
    public void showError(String message) {
        System.out.println(message);
    }
}
