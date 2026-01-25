package peggy;

public enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN;

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
            case "find": return FIND;
            default: return UNKNOWN;
        }
    }
}
