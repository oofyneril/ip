import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

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