package peggy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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

    public static String parseFindKeyword(String input) {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The keyword of a find command cannot be empty.");
        }
        return parts[1].trim();
    }

    public static LocalDateTime parseDateTime(String raw) {
        String s = raw.trim();

        // from Storage: 2019-12-02T18:00
        try { return LocalDateTime.parse(s); } catch (DateTimeParseException ignored) {}

        // user input: 2/12/2019 1800
        try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy HHmm")); }
        catch (DateTimeParseException ignored) {}

        // also allow: 2/12/2019 18:00
        try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")); }
        catch (DateTimeParseException ignored) {}

        // also allow: 2019-12-02 1800
        try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")); }
        catch (DateTimeParseException ignored) {}

        // also allow: 2019-12-02 18:00
        try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
        catch (DateTimeParseException ignored) {}

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

    public static int parseIndex(String input, int size, String cmd) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("Please give a task number, e.g. " + cmd + " 2");
        }

        int idx;
        try {
            idx = Integer.parseInt(parts[1]) - 1; // convert to 0-based
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please give a valid task number, e.g. " + cmd + " 2");
        }

        if (idx < 0 || idx >= size) {
            throw new IllegalArgumentException("Task number out of range.");
        }
        return idx;
    }

    public static String parseTodoDesc(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
        }
        return parts[1].trim();
    }

    public static String[] parseDeadline(String input) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Deadline format: deadline <desc> /by <when>");
        }

        String desc = parts[0].trim();
        String byRaw = parts[1].trim();

        if (desc.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (byRaw.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The date/time of a deadline cannot be empty.");
        }

        // validate
        parseDateTime(byRaw);

        return new String[] { desc, byRaw };
    }

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

        String fromRaw = p2[0].trim();
        String toRaw = p2[1].trim();

        if (desc.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of an event cannot be empty.");
        }
        if (fromRaw.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The 'from time' of an event cannot be empty.");
        }
        if (toRaw.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The 'to time' of an event cannot be empty.");
        }

        LocalDateTime from = parseDateTime(fromRaw);
        LocalDateTime to = parseDateTime(toRaw);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Event time invalid: /from must be earlier than /to.");
        }

        return new String[] { desc, fromRaw, toRaw };
    }
}
