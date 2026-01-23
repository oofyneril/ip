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
        if(!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        for(String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] parts = line.split("\\s*\\|\\s*");
            String type = parts[0];
            boolean done = "1".equals(parts[1]);

            Task t;
            if ("T".equals(type)) {
                t = new ToDo(parts[2]);
            } else if ("D".equals(type)) {
                t = new Deadline(parts[2], parts[3]);
            } else if ("E".equals(type)) {
                t = new Event(parts[2], parts[3], parts[4]);
            } else {
                continue; // skip unknown line
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
        for(Task task : tasks) {
            out.add(format(task));
        }
        Files.write(filePath, out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String format(Task task) {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + done + " | " + task.getDescription();
        }
        if (task instanceof Deadline d) {
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        }
        if (task instanceof Event e) {
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFromTime() + " | " + e.getToTime();
        }
        return "T | " + done + " | " + task.getDescription();
    }
}
