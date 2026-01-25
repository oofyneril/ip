package peggy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import peggy.task.*;

/**
 * Parses raw user input into usable values for command execution.
 * <p>
 *     Responsible for extracting command arguments (e.g., task index, description, date/time tokens).
 *     Throws {@link IllegalArgumentException} for invalid formats.
 * </p>
 */
public class Parser {

    private static final DateTimeFormatter OUT_DATE =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private static final DateTimeFormatter OUT_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm", Locale.ENGLISH);


    // Minimal requirement: accepts yyyy-MM-dd
    public static LocalDate parseDate(String raw) {
        try {
            return LocalDate.parse(raw.trim()); // ISO yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date. Use yyyy-MM-dd (e.g. 2019-10-15).");
        }
    }
    /**
     * Parses a user-provided date/time string into a date/time object.
     * <p>
     * Supports multiple formats depending on your implementation, e.g.:
     * yyyy-MM-dd, yyyy-MM-dd HHmm, d/M/yyyy HHmm, ISO LocalDateTime, etc.
     * </p>
     *
     * @param raw User-supplied date/time string.
     * @return Parsed date/time value (type depends on your code).
     * @throws IllegalArgumentException If the date/time cannot be parsed.
     */
    public static LocalDateTime parseDateTime(String raw) {
        String s = raw.trim();

        // from Storage: 2019-12-02T18:00
        try { return LocalDateTime.parse(s); } catch (DateTimeParseException ignored) {}

        // user input: 2/12/2019 1800  (2 Dec 2019, 18:00)
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        } catch (DateTimeParseException ignored) {}

        // also allow: 2/12/2019 18:00
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"));
        } catch (DateTimeParseException ignored) {}

        // also allow: 2019-12-02 1800
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException ignored) {}

        // also allow: 2019-12-02 18:00
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException ignored) {}

        // date only -> start of day
        try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy")).atStartOfDay(); }
        catch (DateTimeParseException ignored) {}

        try { return LocalDate.parse(s).atStartOfDay(); } // yyyy-MM-dd
        catch (DateTimeParseException ignored) {}

        throw new IllegalArgumentException(
                "Invalid date/time. Try: 2/12/2019 1800 or 2019-12-02 1800"
        );
    }

    public static String formatDate(LocalDate d) {
        return d.format(OUT_DATE);
    }

    public static String formatDateTime(LocalDateTime dt) {
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dt.toLocalDate().format(OUT_DATE);
        }
        return dt.format(OUT_DATE_TIME);
    }

    public static CommandType parseCommandType(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        String cmdWord = parts[0];
        return CommandType.from(cmdWord);
    }
    /**
     * Parses a 1-based task number from user input and converts it to a 0-based index.
     *
     * @param input Full user input (e.g., "mark 2").
     * @param size Current number of tasks in the list.
     * @param cmd Command name (used for error messages).
     * @return 0-based task index.
     * @throws IllegalArgumentException If no number is provided or if index is out of range.
     * @throws NumberFormatException If the provided number is not an integer.
     */
    public static int parseIndex(String input, int size, String cmd) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("Please give a task number, e.g. " + cmd + " 2");
        }
        int idx = Integer.parseInt(parts[1]) - 1; // convert to 0-based
        if (idx < 0 || idx >= size) {
            throw new IllegalArgumentException("Task number out of range.");
        }
        return idx;
    }
    /**
     * Extracts the description for a todo command.
     *
     * @param input Full user input (e.g., "todo read book").
     * @return The todo description.
     * @throws IllegalArgumentException If the description is missing/blank.
     */
    public static String parseTodoDesc(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
        }
        return parts[1].trim();
    }
    /**
     * Parses a deadline command into description and due date/time string.
     *
     * @param input Full user input (e.g., "deadline return book /by 2/12/2019 1800").
     * @return String array of length 2: {description, by}.
     * @throws IllegalArgumentException If format is invalid or parts are missing.
     */
    public static String[] parseDeadline(String input) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Deadline format: deadline <desc> /by <when>");
        }

        String desc = parts[0].trim();
        String by = parts[1].trim();

        if (desc.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (by.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The date of a deadline cannot be empty.");
        }

        return new String[] { desc, by };
    }
    /**
     * Parses an event command into description, start time, and end time strings.
     *
     * @param input Full user input (e.g., "event meeting /from 2019-12-01 1200 /to 2019-12-01 1400").
     * @return String array of length 3: {description, from, to}.
     * @throws IllegalArgumentException If format is invalid or parts are missing.
     */
    public static String[] parseEvent(String input) {
        String rest = input.substring("event".length()).trim();

        String[] p1 = rest.split(" /from ", 2);
        if (p1.length < 2) {
            throw new IllegalArgumentException("Event format: event <desc> /from <start> /to <end>");
        }

        String desc = p1[0].trim();

        String[] p2 = p1[1].split(" /to ", 2);
        if (p2.length < 2) {
            throw new IllegalArgumentException("Event format: event <desc> /from <start> /to <end>");
        }

        String from = p2[0].trim();
        String to = p2[1].trim();

        if (desc.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a event cannot be empty.");
        }
        if (from.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The 'from time' of a event cannot be empty.");
        }
        if (to.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The 'to time' of a event cannot be empty.");
        }

        return new String[] { desc, from, to };
    }
}