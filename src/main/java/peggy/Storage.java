package peggy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import peggy.task.Deadline;
import peggy.task.Event;
import peggy.task.Task;
import peggy.task.ToDo;

/**
 * Handles loading tasks from disk and saving tasks to disk.
 * <p>
 * Uses a simple line-based file format where each line represents one task, e.g.:
 * T | 0 | read book
 * D | 1 | return book | 2019-12-02T18:00
 * E | 0 | meeting | 2019-12-03T10:00 | 2019-12-03T12:00
 * </p>
 */
public class Storage {
    private static final String COMMENT_PREFIX = "#";
    private static final String FIELD_SEPARATOR_REGEX = "\\s*\\|\\s*";

    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";

    private static final String DONE_TRUE = "1";

    private static final int MIN_FIELDS_COMMON = 3;
    private static final int MIN_FIELDS_DEADLINE = 4;
    private static final int MIN_FIELDS_EVENT = 5;

    private final Path filePath;

    /**
     * Creates a storage object that reads/writes tasks from/to a relative file path.
     *
     * @param relativePath Relative path to the storage file (e.g., "data/peggy.txt").
     */
    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return An {@link ArrayList} of tasks loaded from disk.
     * @throws IOException If an I/O error occurs while reading.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        for (String rawLine : lines) {
            Task task = parseLine(rawLine);
            if (task != null) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    /**
     * Saves the given tasks into the storage file, overwriting existing content.
     *
     * @param tasks List of tasks to save.
     * @throws IOException If an I/O error occurs while writing.
     */
    public void save(List<Task> tasks) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        for (Task task : tasks) {
            out.add(format(task));
        }

        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        Files.write(filePath, out,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Parses one line from storage into a {@link Task}.
     *
     * @param rawLine Raw line from file.
     * @return Parsed task, or {@code null} if the line is blank/comment/malformed.
     */
    private Task parseLine(String rawLine) {
        if (rawLine == null) {
            return null;
        }

        String line = rawLine.trim();
        if (line.isEmpty() || line.startsWith(COMMENT_PREFIX)) {
            return null;
        }

        String[] parts = line.split(FIELD_SEPARATOR_REGEX);
        if (parts.length < MIN_FIELDS_COMMON) {
            return null;
        }

        String type = parts[0];
        boolean isDone = DONE_TRUE.equals(parts[1]);

        Task task = parseTaskByType(type, parts);
        if (task == null) {
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private Task parseTaskByType(String type, String[] parts) {
        switch (type) {
        case TYPE_TODO:
            return new ToDo(parts[2]);

        case TYPE_DEADLINE:
            if (parts.length < MIN_FIELDS_DEADLINE) {
                return null;
            }
            return new Deadline(parts[2], parts[3]);

        case TYPE_EVENT:
            if (parts.length < MIN_FIELDS_EVENT) {
                return null;
            }
            return new Event(parts[2], parts[3], parts[4]);

        default:
            return null;
        }
    }

    private String format(Task task) {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + done + " | " + task.getDescription();
        }

        if (task instanceof Deadline deadline) {
            return "D | " + done + " | " + deadline.getDescription() + " | " + deadline.getBy();
        }

        if (task instanceof Event event) {
            return "E | " + done + " | " + event.getDescription()
                    + " | " + event.getFromTime()
                    + " | " + event.getToTime();
        }

        // Fallback: treat unknown task types as ToDo-like
        return "T | " + done + " | " + task.getDescription();
    }
}
