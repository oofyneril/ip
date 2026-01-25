package peggy.task;
/**
 * Represents a task in the chatbot.
 * <p>
 *     A task has a description and a completion status. Subclasses may add extra fields
 *     such as due date/time (Deadline) or start/end time (Event).
 * </p>
 */
public class Task {
    private final String description;
    private boolean isDone;
    /**
     * Creates a task with the given description. The task is initially not done.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }
    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }
    /**
     * Returns the status icon used in printing (X for done, blank for not done).
     *
     * @return "X" if done, otherwise " ".
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }
    /**
     * Returns the description of the task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns whether the task is marked as done.
     *
     * @return True if done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }
    /**
     * Converts the task into a line to be stored in the save file.
     *
     * @return A file-format string representing this task.
     */
    public String toFileString() {
        return "T | " + (isDone ? 1 : 0) + " | " + description;
    }
    /**
     * Returns the human-readable representation of this task for display to the user.
     *
     * @return Display string.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}