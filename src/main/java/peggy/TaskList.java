package peggy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import peggy.task.Task;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        assert tasks != null : "Task list constructor argument should not be null";
        this.tasks = new ArrayList<>(tasks); // defensive copy
    }

    public void add(Task task) {
        assert task != null : "Cannot add null task";
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public TaskList find(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new TaskList();
        }

        String keyLower = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            assert task != null : "Stored task should not be null";
            if (task.getDescription().toLowerCase().contains(keyLower)) {
                matches.add(task);
            }
        }

        return new TaskList(matches);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only view of the tasks for storage/printing purposes.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
