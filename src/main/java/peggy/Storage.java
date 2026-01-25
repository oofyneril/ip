package peggy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import peggy.task.*;
/**
 * Handles loading tasks from disk and saving tasks to disk.
 * <p>
 *     Uses a simple line-based file format where each line represents one task, e.g.:
 *     T | 0 | read book
 *     D | 1 | return book | 2019-12-02T18:00
 *     E | 0 | meeting | 2019-12-03T10:00 | 2019-12-03T12:00
 * </p>
 */
public class Storage {
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
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split("\\s*\\|\\s*");
            if (parts.length < 3) {
                continue; // skip malformed
            }

            String type = parts[0];
            boolean done = "1".equals(parts[1]);

            Task t;
            switch (type) {
                case "T":
                    t = new ToDo(parts[2]);
                    break;

                case "D":
                    if (parts.length < 4) continue;
                    // parts[3] is yyyy-MM-dd (LocalDate.toString())
                    t = new Deadline(parts[2], parts[3]);
                    break;

                case "E":
                    if (parts.length < 5) continue;
                    // parts[3], parts[4] are likely ISO like 2019-12-02T18:00 (LocalDateTime.toString())
                    t = new Event(parts[2], parts[3], parts[4]);
                    break;

                default:
                    continue; // unknown type
            }

            if (done) {
                t.markAsDone();
            }
            tasks.add(t);
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

    private String format(Task task) {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + done + " | " + task.getDescription();
        }
        if (task instanceof Deadline d) {
            // d.getBy() should be LocalDate -> prints yyyy-MM-dd
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        }
        if (task instanceof Event e) {
            // e.getFrom()/getTo() should be LocalDateTime -> prints ISO: 2019-12-02T18:00
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFromTime() + " | " + e.getToTime();
        }

        return "T | " + done + " | " + task.getDescription();
    }
}