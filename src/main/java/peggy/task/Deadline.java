package peggy.task;
import java.time.LocalDateTime;
import peggy.Parser;
/**
 * Represents a deadline task with a due date/time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;
    /**
     * Creates a deadline task.
     *
     * @param description Deadline description.
     * @param by Raw due date/time string from user input (to be parsed/validated).
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = Parser.parseDateTime(by);
    }
    /**
     * Returns the due date/time of the deadline.
     *
     * @return Due date/time (type depends on your implementation).
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone() ? 1 : 0) + " | " + getDescription() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}
