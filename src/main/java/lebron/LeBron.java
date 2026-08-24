package lebron;

import lebron.exception.LeBronException;
import lebron.parser.Parser;
import lebron.storage.Storage;
import lebron.task.TaskList;
import lebron.ui.Ui;

/**
 * Entry point for the LeBron task-manager chatbot. Wires together the
 * Ui, Storage, TaskList, and Parser collaborators and runs the main
 * read-interpret-respond loop until the user types "bye" or input ends.
 */
public class LeBron {
    /** Runs LeBron's read-interpret-respond loop until the user types "bye" or input ends. */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.load());

        ui.showWelcome();
        String input;
        while ((input = ui.readCommand()) != null) {
            if (input.equals("bye")) {
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
}
