package peggy;

import java.util.ArrayList;
import java.util.Objects;

import peggy.task.Deadline;
import peggy.task.Event;
import peggy.task.Task;
import peggy.task.ToDo;

/**
 * Core logic for Peggy. GUI/CLI should call {@link #getResponse(String)}.
 */
public class Peggy {
    private static final String LINE = "---------------------------------------------";

    private static final String MSG_UNKNOWN = "OOPS!!! I don't know what that means :-(";
    private static final String MSG_TRY_HELP = "I don't understand that. Try 'help' to see commands.";

    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_DELETE = "delete";

    private final Storage storage;
    private final TaskList tasks;

    public Peggy(String filePath) {
        this.storage = new Storage(filePath);

        TaskList loaded;
        try {
            ArrayList<Task> list = storage.load();
            Objects.requireNonNull(list, "Storage.load() should not return null");
            loaded = new TaskList(list);
        } catch (Exception e) {
            loaded = new TaskList();
        }
        this.tasks = loaded;
        assert this.tasks != null : "TaskList should be initialized";
    }

    public String getWelcomeMessage() {
        return boxLines(
                "Hello! I'm Peggy, your personal assistant.",
                "",
                "I can manage your todos, deadlines, and events — add them, list them, mark them done, and find them."
        );
    }

    public boolean isExitCommand(String input) {
        if (input == null) {
            return false;
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        String cmdWord = trimmed.split("\\s+")[0];
        return CommandType.from(cmdWord) == CommandType.BYE;
    }

    public String getResponse(String input) {
        if (input == null || input.trim().isBlank()) {
            return formatError(MSG_UNKNOWN);
        }

        String trimmed = input.trim();
        String cmdWord = trimmed.split("\\s+", 2)[0];
        CommandType cmd = CommandType.from(cmdWord);

        switch (cmd) {
            case BYE:
                return boxLines("Bye. Hope to see you again soon!");

            case LIST:
                return formatList();

            case MARK:
                return handleMark(trimmed);

            case UNMARK:
                return handleUnmark(trimmed);

            case DELETE:
                return handleDelete(trimmed);

            case TODO:
                return handleTodo(trimmed);

            case DEADLINE:
                return handleDeadline(trimmed);

            case EVENT:
                return handleEvent(trimmed);

            case FIND:
                return handleFind(trimmed);

            case HELP:
                return formatHelp();

            case HELLO:
                return formatHello();

            default:
                return formatError(MSG_TRY_HELP);
        }
    }

    private String formatList() {
        if (tasks.isEmpty()) {
            return boxLines("Your list is empty.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return box(sb.toString().trim());
    }

    private String formatHello() {
        return boxLines(
                "Hi! How may I assist you? 🙂",
                "Type 'help' to see what I can do."
        );
    }

    private String formatHelp() {
        return boxLines(
                "Here are the commands you can use:",
                "  1) list — view all tasks",
                "  2) todo <description> — add a todo task",
                "  3) deadline <description> /by <time> — add a deadline task",
                "  4) event <description> /from <time> /to <time> — add an event task",
                "  5) mark <task number> — mark a task as done",
                "  6) unmark <task number> — mark a task as not done",
                "  7) delete <task number> — remove a task",
                "  8) find <keyword> — search tasks by keyword",
                "  9) priority <task number> <priority> — set task priority",
                " 10) bye — exit the app"
        );
    }

    private String handleMark(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), CMD_MARK);
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;

            Task t = tasks.get(idx);
            t.markAsDone();
            saveQuietly();

            return boxLines(
                    "Nice! I've marked this task as done:",
                    "  " + t
            );
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleUnmark(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), CMD_UNMARK);
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;

            Task t = tasks.get(idx);
            t.markAsNotDone();
            saveQuietly();

            return boxLines(
                    "OK, I've marked this task as not done yet:",
                    "  " + t
            );
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleDelete(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), CMD_DELETE);
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;

            Task t = tasks.remove(idx);
            saveQuietly();

            return boxLines(
                    "Noted. I've removed this task:",
                    "  " + t,
                    "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleTodo(String input) {
        try {
            String desc = Parser.parseTodoDesc(input);
            Task t = new ToDo(desc);
            assert t != null : "Created ToDo task should not be null";

            tasks.add(t);
            saveQuietly();

            return formatAdded(t);
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleDeadline(String input) {
        try {
            String[] dl = Parser.parseDeadline(input);
            assert dl.length == 2 : "Deadline parse should return [desc, by]";
            assert dl[0] != null && !dl[0].isBlank() : "Deadline description should not be blank";
            assert dl[1] != null && !dl[1].isBlank() : "Deadline by should not be blank";

            Task t = new Deadline(dl[0], dl[1]);
            assert t != null : "Created Deadline task should not be null";

            tasks.add(t);
            saveQuietly();

            return formatAdded(t);
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleEvent(String input) {
        try {
            String[] ev = Parser.parseEvent(input);
            assert ev.length == 3 : "Event parse should return [desc, from, to]";
            assert ev[0] != null && !ev[0].isBlank() : "Event description should not be blank";
            assert ev[1] != null && !ev[1].isBlank() : "Event from should not be blank";
            assert ev[2] != null && !ev[2].isBlank() : "Event to should not be blank";

            Task t = new Event(ev[0], ev[1], ev[2]);
            assert t != null : "Created Event task should not be null";

            tasks.add(t);
            saveQuietly();

            return formatAdded(t);
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String handleFind(String input) {
        try {
            String keyword = Parser.parseFindKeyword(input);
            TaskList matches = tasks.find(keyword);

            if (matches.isEmpty()) {
                return boxLines(
                        "Here are the matching tasks in your list:",
                        "(none)"
                );
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matches.size(); i++) {
                sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
            }
            return box(sb.toString().trim());
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String formatAdded(Task t) {
        return boxLines(
                "Got it. I've added this task:",
                "  " + t,
                "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    private String formatError(String msg) {
        return box(msg);
    }

    private String box(String msg) {
        return LINE + "\n" + msg + "\n" + LINE;
    }

    private String boxLines(String... lines) {
        return box(String.join("\n", lines));
    }

    private void saveQuietly() {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            // In GUI, just show an error response
        }
    }
}
