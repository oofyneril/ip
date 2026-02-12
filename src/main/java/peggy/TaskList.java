package peggy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import peggy.task.Task;

/**
 * Stores and manages the list of tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the given tasks.
     *
     * @param tasks The initial tasks to include.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Initial task list must not be null";
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task";
        tasks.add(task);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index 0-based index.
     * @return The task.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index 0-based index.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Finds tasks whose descriptions contain the given keyword (case-insensitive).
     *
     * @param keyword Keyword to search for.
     * @return A new TaskList containing matching tasks.
     */
    public TaskList find(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new TaskList();
        }

        String keyLower = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyLower)) {
                matches.add(task);
            }
        }

        return new TaskList(matches);
    }

    /**
     * Returns the number of tasks.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns true if there are no tasks.
     *
     * @return Whether empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only view of tasks (for storage/printing).
     *
     * @return Unmodifiable list view.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
