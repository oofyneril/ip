package peggy;
import java.util.ArrayList;
import java.util.List;
import peggy.task.*;
/**
 * Represents the list of tasks managed by the chatbot.
 * <p>
 *     Provides operations to add, remove, and access tasks, while hiding the underlying list structure.
 * </p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    /**
     * Wraps an existing list of tasks.
     *
     * @param tasks The tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    /**
     * Adds a task to the task list.
     *
     * @param t The task to add.
     */
    public void add(Task t) {
        tasks.add(t);
    }
    /**
     * Returns the task at the given index (0-based).
     *
     * @param index 0-based index.
     * @return The task at the index.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public Task get(int index) {
        return tasks.get(index);
    }
    /**
     * Removes and returns the task at the given index (0-based).
     *
     * @param index 0-based index.
     * @return The removed task.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }
    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }
    /**
     * Returns whether the list is empty.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    /**
     * Exposes the underlying tasks as a {@link List} for saving to storage.
     *
     * @return A list view of tasks.
     */
    public List<Task> asList() {
        return tasks;
    }
}

