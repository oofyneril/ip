package peggy.task;
/**
 * Represents a todo task (a task with only a description and done status).
 */
public class ToDo extends Task {
    /**
     * Creates a todo task with the given description.
     *
     * @param description Todo description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone() ? 1 : 0) + " | " + getDescription();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}