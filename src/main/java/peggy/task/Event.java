package peggy.task;
import java.time.LocalDateTime;
import peggy.Parser;

public class Event extends Task {
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;

    public Event(String description, String fromTime, String toTime) {
        super(description);
        this.fromTime = Parser.parseDateTime(fromTime);
        this.toTime = Parser.parseDateTime(toTime);
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }
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
