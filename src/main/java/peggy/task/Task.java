package peggy.task;
import peggy.task.Priority;

/**
 * Represents a task with a description, done status, and optional priority.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;
    private Priority priority;

    /**
     * Creates a task with the given description.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.priority = Priority.NONE;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        if (priority == null) {
            this.priority = Priority.NONE;
            return;
        }
        this.priority = priority;
    }

    /**
     * Converts this task into a line for saving to file.
     *
     * @return File string.
     */
    public abstract String toFileString();

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        String prioritySuffix = priority == Priority.NONE ? "" : " (P:" + priority + ")";
        return "[" + status + "] " + description + prioritySuffix;
    }
}
