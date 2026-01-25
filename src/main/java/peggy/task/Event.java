package peggy.task;
import java.time.LocalDateTime;
import peggy.Parser;
/**
 * Represents an event task with a start and end date/time.
 */
public class Event extends Task {
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;
    /**
     * Creates an event task.
     *
     * @param description Event description.
     * @param fromTime Raw start date/time string.
     * @param toTime Raw end date/time string.
     */
    public Event(String description, String fromTime, String toTime) {
        super(description);
        this.fromTime = Parser.parseDateTime(fromTime);
        this.toTime = Parser.parseDateTime(toTime);
    }
    /**
     * Returns the start date/time of the event.
     *
     * @return Start date/time (type depends on your implementation).
     */
    public LocalDateTime getFromTime() {
        return fromTime;
    }
    /**
     * Returns the end date/time of the event.
     *
     * @return End date/time (type depends on your implementation).
     */
    public LocalDateTime getToTime() {
        return toTime;
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone() ? 1 : 0) + " | " + getDescription()
                + " | " + fromTime + " | " + toTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + Parser.formatDateTime(fromTime)
                + " to: " + Parser.formatDateTime(toTime) + ")";
    }
}
