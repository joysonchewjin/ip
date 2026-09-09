package lebron;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import lebron.exception.LeBronException;
import lebron.parser.Parser;
import lebron.storage.Storage;
import lebron.task.TaskList;
import lebron.ui.Ui;

/**
 * Core orchestrator for the LeBron task-manager chatbot. Wires together the
 * Storage and TaskList collaborators once, then offers two ways to drive
 * them: {@link #run()}, the original console read-interpret-respond loop,
 * and {@link #getResponse(String)}, which lets a GUI process one command at
 * a time and read back the resulting message.
 */
public class LeBron {
    private final Storage storage;
    private final TaskList tasks;

    /** Creates a LeBron backed by the default save file, loading any existing tasks. */
    public LeBron() {
        this(new Storage());
    }

    /** Creates a LeBron backed by the given {@link Storage}, loading any existing tasks. */
    LeBron(Storage storage) {
        this.storage = storage;
        tasks = new TaskList(storage.load());
    }

    /** Entry point for the console (CLI) version of LeBron. */
    public static void main(String[] args) {
        new LeBron().run();
    }

    /**
     * Runs LeBron's console read-interpret-respond loop: greets the user, then
     * repeatedly reads a line of input and hands it to {@link Parser#execute},
     * printing any {@link LeBronException} as an error, until the user types
     * "bye" or input ends.
     */
    public void run() {
        Ui ui = new Ui();
        ui.showWelcome();
        String input;
        while ((input = ui.readCommand()) != null) {
            if (isExit(input)) {
                break;
            }
            try {
                Parser.execute(input, tasks, ui, storage);
            } catch (LeBronException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.showGoodbye();
    }

    /**
     * Runs a single line of input through the same command logic as
     * {@link #run()}, returning the resulting message instead of printing it
     * to the console, so a GUI can display it as a dialog bubble.
     *
     * @return the message LeBron would otherwise have printed for {@code input}.
     */
    public String getResponse(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Ui ui = new Ui(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        if (isExit(input)) {
            ui.showGoodbye();
        } else {
            try {
                Parser.execute(input, tasks, ui, storage);
            } catch (LeBronException e) {
                ui.showError(e.getMessage());
            }
        }
        return buffer.toString(StandardCharsets.UTF_8).trim();
    }

    /** Returns true if {@code input} is the command that ends the session. */
    public boolean isExit(String input) {
        return input.equals("bye");
    }
}
