package lebron.ui;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import lebron.exception.LeBronException;
import lebron.task.Task;

/**
 * Handles all interaction with the user: printing the greeting, farewell,
 * task list, and confirmation/error messages, and reading command input
 * from the console. Where output is printed to is configurable (defaulting
 * to {@code System.out}), so a GUI can capture the same message text by
 * constructing a {@link Ui} backed by a different {@link PrintStream}.
 */
public class Ui {
    private static final String NAME = "LeBron";

    /** Horizontal divider printed around each block of chatbot output. */
    private static final String LINE = "____________________________________________________________";

    /** ASCII art banner displayed on startup, spelling "LEBRON" in block letters. */
    private static final String BANNER =
            """
            #     ##### ####  ####   ###  #   #
            #     #     #   # #   # #   # ##  #
            #     ###   ####  ####  #   # # # #
            #     #     #   # #  #  #   # #  ##
            ##### ##### ####  #   #  ###  #   #
            """;

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out;

    /** Creates a Ui that prints to {@code System.out}, for the console version of LeBron. */
    public Ui() {
        this(System.out);
    }

    /** Creates a Ui that prints to {@code out} instead, e.g. a buffer a GUI reads back from. */
    public Ui(PrintStream out) {
        this.out = out;
    }

    /** Prints the startup banner and greeting, wrapped in divider lines. */
    public void showWelcome() {
        out.println(LINE);
        out.print(BANNER);
        out.println("Hello! I'm " + NAME + ".");
        out.println("What can I do for you?");
        out.println(LINE);
    }

    /** Prints the farewell message, wrapped in a divider line. */
    public void showGoodbye() {
        out.println("Bye. Hope to see you again soon!");
        out.println(LINE);
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
        out.println(LINE);
    }

    /** Prints all tasks in {@code tasks} as a numbered list, including their done status. */
    public void showTaskList(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Prints {@code tasks} as a numbered list under a "matching tasks" header. */
    public void showMatchingTasks(List<Task> tasks) {
        out.println("Here are the matching tasks in your list:");
        showTaskList(tasks);
    }

    /** Prints an acknowledgement that {@code task} was added. */
    public void showAdded(Task task) {
        out.println("added: " + task);
    }

    /** Prints a confirmation that {@code task} was marked as done. */
    public void showMarked(Task task) {
        out.println("Nice! I've marked this task as done:");
        out.println("  " + task);
    }

    /** Prints a confirmation that {@code task} was marked as not done. */
    public void showUnmarked(Task task) {
        out.println("OK, I've marked this task as not done yet:");
        out.println("  " + task);
    }

    /** Prints a confirmation that {@code task} was removed, and how many tasks remain. */
    public void showDeleted(Task task, int remainingCount) {
        out.println("Noted. I've removed this task:");
        out.println("  " + task);
        out.println("Now you have " + remainingCount + " tasks in the list.");
    }

    /** Prints an error message, e.g. from a caught {@link LeBronException}. */
    public void showError(String message) {
        out.println(message);
    }
}
