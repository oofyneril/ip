package peggy;
/**
 * Represents the supported command keywords that Peggy can understand.
 * <p>
 *     The {@link #from(String)} method maps the first word of user input to a corresponding
 *     {@code CommandType}. Unrecognized commands map to {@link #UNKNOWN}.
 * </p>
 */
public enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN;
    /**
     * Converts a command word into a {@code CommandType}.
     *
     * @param word The first token of the user input (e.g., {@code "list"}).
     * @return The matching {@code CommandType}, or {@link #UNKNOWN} if there is no match.
     */
    public static CommandType from(String word) {
        if (word == null) {
            return UNKNOWN;
        }
        switch (word) {
            case "bye": return BYE;
            case "list": return LIST;
            case "mark": return MARK;
            case "unmark": return UNMARK;
            case "todo": return TODO;
            case "deadline": return DEADLINE;
            case "event": return EVENT;
            case "delete": return DELETE;
            default: return UNKNOWN;
        }
    }
}
