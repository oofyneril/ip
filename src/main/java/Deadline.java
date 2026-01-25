import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String description, String by) {
        super(description);
        this.by = Parser.parseDateTime(by);
    }

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
