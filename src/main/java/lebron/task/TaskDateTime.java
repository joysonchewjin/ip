package lebron.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lebron.exception.LeBronException;

/**
 * An immutable date, optionally with a time of day, used for {@link Deadline}'s
 * "by" field and {@link Event}'s "from"/"to" fields. Wraps a {@link LocalDateTime}
 * together with a flag remembering whether a time was actually given, so that a
 * date-only value (e.g. "2/12/2019") can be told apart from midnight on that date
 * when displaying or saving it.
 */
public final class TaskDateTime {
    /** User-facing input format with a time, e.g. "2/12/2019 1800". */
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /** User-facing input format without a time, e.g. "2/12/2019". */
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("d/M/yyyy");

    /** Display format for the date part, e.g. "Dec 02 2019". */
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** Display format for the time part, e.g. "6:00PM" (lowercased before use). */
    private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("h:mma");

    private final LocalDateTime dateTime;
    private final boolean hasTime;

    private TaskDateTime(LocalDateTime dateTime, boolean hasTime) {
        this.dateTime = dateTime;
        this.hasTime = hasTime;
    }

    /**
     * Parses user-typed input such as "2/12/2019" or "2/12/2019 1800".
     *
     * @throws LeBronException if the text matches neither format.
     */
    public static TaskDateTime parseInput(String text) throws LeBronException {
        return parse(text, INPUT_DATE_TIME, INPUT_DATE,
                "Shot clock violation! I don't recognize '" + text + "' as a date. "
                        + "Use d/M/yyyy or d/M/yyyy HHmm, e.g. 2/12/2019 or 2/12/2019 1800.");
    }

    /**
     * Parses a value previously written by {@link #toStorageString()}, i.e. the
     * ISO format {@link LocalDateTime}/{@link LocalDate} produce on their own.
     *
     * @throws LeBronException if the text matches neither format.
     */
    public static TaskDateTime parseStorage(String text) throws LeBronException {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE,
                "Malformed date in save file: " + text);
    }

    /** Tries {@code dateTimeFormat} first, then falls back to date-only {@code dateFormat}. */
    private static TaskDateTime parse(String text, DateTimeFormatter dateTimeFormat,
            DateTimeFormatter dateFormat, String errorMessage) throws LeBronException {
        try {
            return new TaskDateTime(LocalDateTime.parse(text, dateTimeFormat), true);
        } catch (DateTimeParseException e) {
            try {
                return new TaskDateTime(LocalDate.parse(text, dateFormat).atStartOfDay(), false);
            } catch (DateTimeParseException e2) {
                throw new LeBronException(errorMessage);
            }
        }
    }

    /**
     * Returns a stable, unambiguous representation suitable for saving to disk and
     * reading back with {@link #parseStorage(String)}, independent of the format the
     * user originally typed.
     */
    public String toStorageString() {
        return hasTime ? dateTime.toString() : dateTime.toLocalDate().toString();
    }

    /** Returns the friendly display form, e.g. "Dec 02 2019, 6:00pm" or "Dec 02 2019". */
    @Override
    public String toString() {
        String date = dateTime.format(DISPLAY_DATE);
        if (!hasTime) {
            return date;
        }
        String time = dateTime.format(DISPLAY_TIME).toLowerCase();
        return date + ", " + time;
    }
}
