package lebron.parser;

import lebron.exception.LeBronException;
import lebron.storage.Storage;
import lebron.task.Deadline;
import lebron.task.Event;
import lebron.task.Task;
import lebron.task.TaskDateTime;
import lebron.task.TaskList;
import lebron.task.Todo;
import lebron.ui.Ui;

/**
 * Interprets a single line of user input and carries out the corresponding
 * action against a {@link TaskList}, {@link Ui}, and {@link Storage}: it
 * both makes sense of the command and executes it, so that {@link lebron.LeBron}'s
 * main loop only needs to hand each line off and catch {@link LeBronException}.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Interprets {@code input} (anything other than "bye", which the caller
     * handles itself) and performs the action it names: printing the task
     * list, marking/unmarking or deleting a task, or adding a new todo,
     * deadline, or event. Any mutation is persisted to {@code storage}
     * immediately afterwards, matching the original behavior of saving
     * after every change.
     *
     * @throws LeBronException if the command is malformed, refers to a task
     *     number that doesn't exist, or isn't recognized at all.
     */
    public static void execute(String input, TaskList tasks, Ui ui, Storage storage) throws LeBronException {
        if (input.equals("list")) {
            ui.showTaskList(tasks.getAll());
        } else if (input.startsWith("mark ")) {
            int index = parseIndex(input.substring("mark ".length()), "mark <task number>");
            Task task = tasks.mark(index);
            ui.showMarked(task);
            storage.save(tasks.getAll());
        } else if (input.startsWith("unmark ")) {
            // Usage hint intentionally says "mark", matching the original app's behavior.
            int index = parseIndex(input.substring("unmark ".length()), "mark <task number>");
            Task task = tasks.unmark(index);
            ui.showUnmarked(task);
            storage.save(tasks.getAll());
        } else if (input.startsWith("delete ")) {
            int index = parseIndex(input.substring("delete ".length()), "delete <task number>");
            Task task = tasks.delete(index);
            ui.showDeleted(task, tasks.size());
            storage.save(tasks.getAll());
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            Task task = parseTodo(input.equals("todo") ? "" : input.substring("todo ".length()));
            tasks.add(task);
            ui.showAdded(task);
            storage.save(tasks.getAll());
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            Task task = parseDeadline(input.equals("deadline") ? "" : input.substring("deadline ".length()));
            tasks.add(task);
            ui.showAdded(task);
            storage.save(tasks.getAll());
        } else if (input.equals("event") || input.startsWith("event ")) {
            Task task = parseEvent(input.equals("event") ? "" : input.substring("event ".length()));
            tasks.add(task);
            ui.showAdded(task);
            storage.save(tasks.getAll());
        } else if (input.equals("find") || input.startsWith("find ")) {
            String keyword = parseFindKeyword(input.equals("find") ? "" : input.substring("find ".length()));
            ui.showMatchingTasks(tasks.find(keyword));
        } else {
            throw new LeBronException("That's out of bounds — I don't recognize that command: " + input);
        }
    }

    /**
     * Parses a 1-based task number as typed by the user into a 0-based index.
     *
     * @param usageHint the "command <task number>" text shown if {@code indexText} isn't a number.
     * @throws LeBronException if {@code indexText} isn't a valid integer.
     */
    private static int parseIndex(String indexText, String usageHint) throws LeBronException {
        try {
            return Integer.parseInt(indexText) - 1;
        } catch (NumberFormatException e) {
            throw new LeBronException("That's not on the scoreboard — I don't recognize '"
                    + indexText + "' as a task number: " + usageHint);
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
        return new Event(description, TaskDateTime.parseInput(from), TaskDateTime.parseInput(to));
    }

    /**
     * Parses the text after "find " into a search keyword.
     *
     * @throws LeBronException if the keyword is missing.
     */
    private static String parseFindKeyword(String args) throws LeBronException {
        String keyword = args.trim();
        if (keyword.isEmpty()) {
            throw new LeBronException("Airball! A find needs a keyword: find <keyword>");
        }
        return keyword;
    }
}
