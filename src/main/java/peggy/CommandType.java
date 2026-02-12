package peggy;

public enum CommandType {
    BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, HELP, HELLO, Priority, UNKNOWN;

    public static CommandType from(String word) {
        if (word == null) return UNKNOWN;

        switch (word.toLowerCase()) {
        case "bye": return BYE;
        case "list": return LIST;
        case "mark": return MARK;
        case "unmark": return UNMARK;
        case "delete": return DELETE;
        case "todo": return TODO;
        case "deadline": return DEADLINE;
        case "event": return EVENT;
        case "find": return FIND;
        case "help": return HELP;
        case "priority": return Priority;
        case "hi":
        case "hello":
            return HELLO;
        default:
            return UNKNOWN;
        }
    }
}
