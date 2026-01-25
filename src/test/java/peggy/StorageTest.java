package peggy;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import peggy.task.*; // Task, ToDo, Deadline, Event

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveThenLoad_roundTrip_preservesTasks() throws Exception {
        Path file = tempDir.resolve("peggy.txt");
        Storage storage = new Storage(file.toString());

        ArrayList<Task> original = new ArrayList<>();

        Task t1 = new ToDo("read book");
        t1.markAsDone();
        original.add(t1);

        Task t2 = new Deadline("return book", "2019-12-02T18:00"); // ISO is safe for saving/loading
        original.add(t2);

        Task t3 = new Event("project meeting", "2019-12-03T10:00", "2019-12-03T12:00");
        original.add(t3);

        storage.save(original);
        ArrayList<Task> loaded = storage.load();

        assertEquals(3, loaded.size());

        assertTrue(loaded.get(0) instanceof ToDo);
        assertEquals("read book", loaded.get(0).getDescription());
        assertTrue(loaded.get(0).isDone());

        assertTrue(loaded.get(1) instanceof Deadline);
        assertEquals("return book", loaded.get(1).getDescription());
        assertFalse(loaded.get(1).isDone());

        assertTrue(loaded.get(2) instanceof Event);
        assertEquals("project meeting", loaded.get(2).getDescription());

        // also verify file format roughly
        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("T | 1 | read book"));
        assertTrue(lines.get(1).contains("D | 0 | return book"));
        assertTrue(lines.get(2).contains("E | 0 | project meeting"));
    }
}
