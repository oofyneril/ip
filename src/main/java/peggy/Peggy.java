package peggy;

import java.util.ArrayList;

import peggy.task.Deadline;
import peggy.task.Event;
import peggy.task.Task;
import peggy.task.ToDo;

/**
 * Core logic for Peggy. GUI/CLI should call {@link #getResponse(String)}.
 */
public class Peggy {
    private static final String LINE = "---------------------------------------------";

    private final Storage storage;
    private final TaskList tasks;

    public Peggy(String filePath) {
        this.storage = new Storage(filePath);

        TaskList loaded;
        try {
            ArrayList<Task> list = storage.load();
            loaded = new TaskList(list);
        } catch (Exception e) {
            loaded = new TaskList();
        }
        this.tasks = loaded;
    }

    public String getWelcomeMessage() {
        return LINE + "\n"
                + "Hello! I'm Peggy\n"
                + "What can I do for you?\n"
                + LINE;
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
        if (input == null) {
            return formatError("OOPS!!! I don't know what that means :-(");
        }

        String trimmed = input.trim();
        if (trimmed.isBlank()) {
            return formatError("OOPS!!! I don't know what that means :-(");
        }

        String cmdWord = trimmed.split("\\s+", 2)[0];
        CommandType cmd = CommandType.from(cmdWord);

        switch (cmd) {
            case BYE:
                return LINE + "\n"
                        + "Bye. Hope to see you again soon!\n"
                        + LINE;

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
                return formatError("I don't understand that. Try 'help' to see commands.");
        }
    }

    private String formatList() {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append("\n");
        if (tasks.isEmpty()) {
            sb.append("Your list is empty.\n");
        } else {
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
        }
        sb.append(LINE);
        return sb.toString();
    }

    private String formatHello() {
        return LINE + "\n"
                + "Hi! 🙂\n"
                + "Type 'help' to see what I can do.\n"
                + LINE;
    }

    private String formatHelp() {
        return LINE + "\n"
                + "Here are the commands you can use:\n"
                + "  list\n"
                + "  todo <description>\n"
                + "  deadline <description> /by <time>\n"
                + "  event <description> /from <time> /to <time>\n"
                + "  mark <task number>\n"
                + "  unmark <task number>\n"
                + "  delete <task number>\n"
                + "  find <keyword>\n"
                + "  bye\n"
                + LINE;
    }


    private String handleMark(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), "mark");
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;
            Task t = tasks.get(idx);
            t.markAsDone();
            saveQuietly();

            return LINE + "\n"
                    + "Nice! I've marked this task as done:\n"
                    + "  " + t + "\n"
                    + LINE;
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        } catch (Exception e) {
            return formatError("Please give a valid task number, e.g. mark 2");
        }
    }

    private String handleUnmark(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), "unmark");
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;
            Task t = tasks.get(idx);
            t.markAsNotDone();
            saveQuietly();

            return LINE + "\n"
                    + "OK, I've marked this task as not done yet:\n"
                    + "  " + t + "\n"
                    + LINE;
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        } catch (Exception e) {
            return formatError("Please give a valid task number, e.g. unmark 2");
        }
    }

    private String handleDelete(String input) {
        try {
            int idx = Parser.parseIndex(input, tasks.size(), "delete");
            assert idx >= 0 && idx < tasks.size() : "Parser returned out-of-range index: " + idx;
            Task t = tasks.remove(idx);
            saveQuietly();

            return LINE + "\n"
                    + "Noted. I've removed this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.\n"
                    + LINE;
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        } catch (Exception e) {
            return formatError("Please give a valid task number, e.g. delete 2");
        }
    }

    private String handleTodo(String input) {
        try {
            String desc = Parser.parseTodoDesc(input);
            Task t = new ToDo(desc);
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
            Task t = new Deadline(dl[0], dl[1]);
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
            Task t = new Event(ev[0], ev[1], ev[2]);
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

            StringBuilder sb = new StringBuilder();
            sb.append(LINE).append("\n");
            sb.append("Here are the matching tasks in your list:\n");
            if (matches.isEmpty()) {
                sb.append("(none)\n");
            } else {
                for (int i = 0; i < matches.size(); i++) {
                    sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
                }
            }
            sb.append(LINE);
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return formatError(e.getMessage());
        }
    }

    private String formatAdded(Task t) {
        return LINE + "\n"
                + "Got it. I've added this task:\n"
                + "  " + t + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.\n"
                + LINE;
    }

    private String formatError(String msg) {
        return LINE + "\n" + msg + "\n" + LINE;
    }

    private void saveQuietly() {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            // In GUI, just show an error response
        }
    }
}
