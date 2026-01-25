package peggy;
import java.util.ArrayList;
import peggy.task.*;

/**
 * Entry point of the Peggy chatbot application
 * <p>
 *     Loads tasks from storage, then repeatedly reads user commands, parses them,
 *     executes the requested action, and saves updates back to disk.
 * </p>
 */
public class Peggy {
    /**
     * Starts the chatbot program.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        UI ui = new UI();
        Storage storage = new Storage("data/peggy.txt");

        TaskList tasks;
        try {
            ArrayList<Task> loaded = storage.load();
            tasks = new TaskList(loaded);
        } catch (Exception e) {
            tasks = new TaskList();
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            if (input.isBlank()) {
                ui.showError("OOPS!!! I don't know what that means :-(");
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            String cmdWord = parts[0];
            CommandType cmd = CommandType.from(cmdWord);

            switch (cmd) {
                case BYE:
                    ui.showBye();
                    ui.close();
                    return;

                case LIST:
                    ui.showList(tasks);
                    break;

                case MARK:
                    try {
                        int idx = Parser.parseIndex(input, tasks.size(), "mark");
                        Task t = tasks.get(idx);
                        t.markAsDone();
                        saveQuietly(storage, tasks, ui);
                        ui.showMarked(t);
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    } catch (Exception e) {
                        ui.showError("Please give a valid task number, e.g. mark 2");
                    }
                    break;

                case UNMARK:
                    try {
                        int idx = Parser.parseIndex(input, tasks.size(), "unmark");
                        Task t = tasks.get(idx);
                        t.markAsNotDone();
                        saveQuietly(storage, tasks, ui);
                        ui.showUnmarked(t);
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    } catch (Exception e) {
                        ui.showError("Please give a valid task number, e.g. unmark 2");
                    }
                    break;

                case DELETE:
                    try {
                        int idx = Parser.parseIndex(input, tasks.size(), "delete");
                        Task t = tasks.remove(idx);
                        saveQuietly(storage, tasks, ui);
                        ui.showDeleted(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    } catch (Exception e) {
                        ui.showError("Please give a valid task number, e.g. delete 2");
                    }
                    break;

                case TODO:
                    try {
                        String desc = Parser.parseTodoDesc(input);
                        Task t = new ToDo(desc);
                        tasks.add(t);
                        saveQuietly(storage, tasks, ui);
                        ui.showAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    }
                    break;

                case DEADLINE:
                    try {
                        String[] dl = Parser.parseDeadline(input);
                        Task t = new Deadline(dl[0], dl[1]);
                        tasks.add(t);
                        saveQuietly(storage, tasks, ui);
                        ui.showAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    }
                    break;

                case EVENT:
                    try {
                        String[] ev = Parser.parseEvent(input);
                        Task t = new Event(ev[0], ev[1], ev[2]);
                        tasks.add(t);
                        saveQuietly(storage, tasks, ui);
                        ui.showAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    }
                    break;

                case FIND:
                    try {
                        String keyword = Parser.parseFindKeyword(input);
                        TaskList matches = tasks.find(keyword);
                        ui.showFindResults(matches);
                    } catch (IllegalArgumentException e) {
                        ui.showError(e.getMessage());
                    }
                    break;

                default:
                    ui.showError("OOPS!!! I don't know what that means :-(");
                    break;
            }
        }
    }
    private static void saveQuietly(Storage storage, TaskList tasks, UI UI) {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            UI.showError("OOPS!!! I couldn't save your tasks to disk.");
        }
    }
}